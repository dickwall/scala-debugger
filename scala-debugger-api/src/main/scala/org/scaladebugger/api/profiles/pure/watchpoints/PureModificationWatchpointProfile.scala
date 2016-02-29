package org.scaladebugger.api.profiles.pure.watchpoints
import acyclic.file

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

import com.sun.jdi.event.ModificationWatchpointEvent
import org.scaladebugger.api.lowlevel.JDIArgument
import org.scaladebugger.api.lowlevel.events.EventType.ModificationWatchpointEventType
import org.scaladebugger.api.lowlevel.events.filters.UniqueIdPropertyFilter
import org.scaladebugger.api.lowlevel.events.{EventManager, JDIEventArgument}
import org.scaladebugger.api.lowlevel.requests.JDIRequestArgument
import org.scaladebugger.api.lowlevel.requests.properties.UniqueIdProperty
import org.scaladebugger.api.lowlevel.utils.JDIArgumentGroup
import org.scaladebugger.api.lowlevel.watchpoints._
import org.scaladebugger.api.pipelines.Pipeline
import org.scaladebugger.api.pipelines.Pipeline.IdentityPipeline
import org.scaladebugger.api.profiles.Constants._
import org.scaladebugger.api.profiles.traits.watchpoints.ModificationWatchpointProfile
import org.scaladebugger.api.utils.{Memoization, MultiMap}

import scala.collection.JavaConverters._
import scala.util.Try

/**
 * Represents a pure profile for modification watchpoints that adds no
 * extra logic on top of the standard JDI.
 */
trait PureModificationWatchpointProfile extends ModificationWatchpointProfile {
  protected val modificationWatchpointManager: ModificationWatchpointManager
  protected val eventManager: EventManager

  /**
   * Retrieves the collection of active and pending modification watchpoint requests.
   *
   * @return The collection of information on modification watchpoint requests
   */
  override def modificationWatchpointRequests: Seq[ModificationWatchpointRequestInfo] = {
    modificationWatchpointManager.modificationWatchpointRequestList ++ (modificationWatchpointManager match {
      case p: PendingModificationWatchpointSupportLike  => p.pendingModificationWatchpointRequests
      case _                                            => Nil
    })
  }

  /**
   * Contains a mapping of request ids to associated event handler ids.
   */
  private val pipelineRequestEventIds = new MultiMap[String, String]

  /**
   * Contains mapping from input to a counter indicating how many pipelines
   * are currently active for the input.
   */
  private val pipelineCounter = new ConcurrentHashMap[
    (String, String, Seq[JDIEventArgument]),
    AtomicInteger
  ]().asScala

  /**
   * Constructs a stream of modification watchpoint events for field in the
   * specified class.
   *
   * @param className The full name of the class whose field to watch
   * @param fieldName The name of the field to watch
   * @param extraArguments The additional JDI arguments to provide
   *
   * @return The stream of modification watchpoint events and any retrieved data
   *         based on requests from extra arguments
   */
  override def onModificationWatchpointWithData(
    className: String,
    fieldName: String,
    extraArguments: JDIArgument*
  ): Try[IdentityPipeline[ModificationWatchpointEventAndData]] = Try {
    val JDIArgumentGroup(rArgs, eArgs, _) = JDIArgumentGroup(extraArguments: _*)
    val requestId = newModificationWatchpointRequest((
      className,
      fieldName,
      rArgs
    ))
    newModificationWatchpointPipeline(requestId, (className, fieldName, eArgs))
  }

  /**
   * Creates a new modification watchpoint request using the given arguments.
   * The request is memoized, meaning that the same request will be returned for
   * the same arguments. The memoized result will be thrown out if the
   * underlying request storage indicates that the request has been removed.
   *
   * @return The id of the created modification watchpoint request
   */
  protected val newModificationWatchpointRequest = {
    type Input = (String, String, Seq[JDIRequestArgument])
    type Key = (String, String, Seq[JDIRequestArgument])
    type Output = String

    new Memoization[Input, Key, Output](
      memoFunc = (input: Input) => {
        val requestId = newModificationWatchpointRequestId()
        val args = UniqueIdProperty(id = requestId) +: input._3

        modificationWatchpointManager.createModificationWatchpointRequestWithId(
          requestId,
          input._1,
          input._2,
          args: _*
        ).get

        requestId
      },
      cacheInvalidFunc = (key: Key) => {
        !modificationWatchpointManager.hasModificationWatchpointRequest(
          key._1,
          key._2
        )
      }
    )
  }

  /**
   * Creates a new pipeline of modification watchpoint events and data using
   * the given arguments. The pipeline is NOT memoized; therefore, each call
   * creates a new pipeline with a new underlying event handler feeding the
   * pipeline. This means that the pipeline needs to be properly closed to
   * remove the event handler.
   *
   * @param requestId The id of the request whose events to stream through the
   *                  new pipeline
   * @param args The additional event arguments to provide to the event handler
   *             feeding the new pipeline
   * @return The new modification watchpoint event and data pipeline
   */
  protected def newModificationWatchpointPipeline(
    requestId: String,
    args: (String, String, Seq[JDIEventArgument])
    ): IdentityPipeline[ModificationWatchpointEventAndData] = {
    val eArgsWithFilter = UniqueIdPropertyFilter(id = requestId) +: args._3
    val newPipeline = eventManager
      .addEventDataStream(ModificationWatchpointEventType, eArgsWithFilter: _*)
      .map(t => (t._1.asInstanceOf[ModificationWatchpointEvent], t._2))
      .noop()

    // Create a companion pipeline who, when closed, checks to see if there
    // are no more pipelines for the given request and, if so, removes the
    // request as well
    val closePipeline = Pipeline.newPipeline(
      classOf[ModificationWatchpointEventAndData],
      newModificationWatchpointPipelineCloseFunc(requestId, args)
    )

    // Increment the counter for open pipelines
    pipelineCounter
      .getOrElseUpdate(args, new AtomicInteger(0))
      .incrementAndGet()

    val combinedPipeline = newPipeline.unionOutput(closePipeline)

    // Store the new event handler id as associated with the current request
    pipelineRequestEventIds.put(
      requestId,
      combinedPipeline.currentMetadata(
        EventManager.EventHandlerIdMetadataField
      ).asInstanceOf[String]
    )

    combinedPipeline
  }

  /**
   * Creates a new function used for closing generated pipelines.
   *
   * @param requestId The id of the request
   * @param args The arguments associated with the request
   *
   * @return The new function for closing the pipeline
   */
  protected def newModificationWatchpointPipelineCloseFunc(
    requestId: String,
    args: (String, String, Seq[JDIEventArgument])
  ): (Option[Any]) => Unit = (data: Option[Any]) => {
    val pCounter = pipelineCounter(args)

    val totalPipelinesRemaining = pCounter.decrementAndGet()

    if (totalPipelinesRemaining == 0 || data.exists(_ == CloseRemoveAll)) {
      modificationWatchpointManager.removeModificationWatchpointRequestWithId(
        requestId
      )
      pipelineRequestEventIds.remove(requestId).foreach(
        _.foreach(eventManager.removeEventHandler)
      )
      pCounter.set(0)
    }
  }

  /**
   * Used to generate new request ids to capture request/event matches.
   *
   * @return The new id as a string
   */
  protected def newModificationWatchpointRequestId(): String =
    java.util.UUID.randomUUID().toString
}
