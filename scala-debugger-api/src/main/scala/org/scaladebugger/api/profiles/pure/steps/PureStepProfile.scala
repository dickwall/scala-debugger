package org.scaladebugger.api.profiles.pure.steps
import acyclic.file

import com.sun.jdi.ThreadReference
import com.sun.jdi.event.StepEvent
import org.scaladebugger.api.lowlevel.JDIArgument
import org.scaladebugger.api.lowlevel.events.EventType.StepEventType
import org.scaladebugger.api.lowlevel.events.data.JDIEventDataResult
import org.scaladebugger.api.lowlevel.events.{EventManager, JDIEventArgument}
import org.scaladebugger.api.lowlevel.requests.JDIRequestArgument
import org.scaladebugger.api.lowlevel.requests.filters.ThreadFilter
import org.scaladebugger.api.lowlevel.steps._
import org.scaladebugger.api.lowlevel.utils.JDIArgumentGroup
import org.scaladebugger.api.pipelines.Pipeline.IdentityPipeline
import org.scaladebugger.api.profiles.traits.steps.StepProfile

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

/**
 * Represents a pure profile for steps that adds no
 * extra logic on top of the standard JDI.
 */
trait PureStepProfile extends StepProfile {
  protected val stepManager: StepManager
  protected val eventManager: EventManager

  /**
   * Retrieves the collection of active and pending step requests.
   *
   * @return The collection of information on step requests
   */
  override def stepRequests: Seq[StepRequestInfo] = {
    stepManager.stepRequestList ++ (stepManager match {
      case p: PendingStepSupportLike  => p.pendingStepRequests
      case _                          => Nil
    })
  }

  /**
   * Steps in from the current location to the next line.
   *
   * @param threadReference The thread in which to perform the step
   * @param extraArguments The additional JDI arguments to provide
   *
   * @return The resulting event and any retrieved data based on
   *         requests from extra arguments
   */
  override def stepIntoLineWithData(
    threadReference: ThreadReference,
    extraArguments: JDIArgument*
  ): Future[StepEventAndData] = createStepFuture(
    stepManager.createStepIntoLineRequest,
    threadReference,
    extraArguments: _*
  )

  /**
   * Steps over from the current location to the next line.
   *
   * @param threadReference The thread in which to perform the step
   * @param extraArguments The additional JDI arguments to provide
   *
   * @return The resulting event and any retrieved data based on
   *         requests from extra arguments
   */
  override def stepOverLineWithData(
    threadReference: ThreadReference,
    extraArguments: JDIArgument*
  ): Future[StepEventAndData] = createStepFuture(
    stepManager.createStepOverLineRequest,
    threadReference,
    extraArguments: _*
  )

  /**
   * Constructs a stream of step events caused by stepping out from the
   * current location to the next line.
   *
   * @param threadReference The thread in which to perform the step
   * @param extraArguments The additional JDI arguments to provide
   *
   * @return The resulting event and any retrieved data based on
   *         requests from extra arguments
   */
  override def stepOutLineWithData(
    threadReference: ThreadReference,
    extraArguments: JDIArgument*
  ): Future[StepEventAndData] = createStepFuture(
    stepManager.createStepOutLineRequest,
    threadReference,
    extraArguments: _*
  )

  /**
   * Steps in from the current location to the next location.
   *
   * @param threadReference The thread in which to perform the step
   * @param extraArguments The additional JDI arguments to provide
   *
   * @return The resulting event and any retrieved data based on
   *         requests from extra arguments
   */
  override def stepIntoMinWithData(
    threadReference: ThreadReference,
    extraArguments: JDIArgument*
  ): Future[StepEventAndData] = createStepFuture(
    stepManager.createStepIntoMinRequest,
    threadReference,
    extraArguments: _*
  )

  /**
   * Steps over from the current location to the next location.
   *
   * @param threadReference The thread in which to perform the step
   * @param extraArguments The additional JDI arguments to provide
   *
   * @return The resulting event and any retrieved data based on
   *         requests from extra arguments
   */
  override def stepOverMinWithData(
    threadReference: ThreadReference,
    extraArguments: JDIArgument*
  ): Future[StepEventAndData] = createStepFuture(
    stepManager.createStepOverMinRequest,
    threadReference,
    extraArguments: _*
  )

  /**
   * Constructs a stream of step events caused by stepping out from the
   * current location to the next location.
   *
   * @param threadReference The thread in which to perform the step
   * @param extraArguments The additional JDI arguments to provide
   *
   * @return The resulting event and any retrieved data based on
   *         requests from extra arguments
   */
  override def stepOutMinWithData(
    threadReference: ThreadReference,
    extraArguments: JDIArgument*
  ): Future[StepEventAndData] = createStepFuture(
    stepManager.createStepOutMinRequest,
    threadReference,
    extraArguments: _*
  )

  /**
   * Constructs a stream of step events.
   *
   * @param threadReference The thread with which to receive step events
   * @param extraArguments The additional JDI arguments to provide
   *
   * @return The stream of step events and any retrieved data based on
   *         requests from extra arguments
   */
  override def onStepWithData(
    threadReference: ThreadReference,
    extraArguments: JDIArgument*
  ): Try[IdentityPipeline[(StepEvent, Seq[JDIEventDataResult])]] = Try {
    val JDIArgumentGroup(_, eArgs, _) = JDIArgumentGroup(extraArguments: _*)
    newStepPipeline((threadReference, eArgs))
  }

  /**
   * Creates a new step request and constructs a future for when its result
   * returns.
   *
   * @param newStepRequestFunc The function used to create the request and
   *                           return the id of the request
   * @param threadReference The thread in which to perform the step
   * @param extraArguments The additional JDI arguments to provide
   *
   * @return The future containing the result from the step request
   */
  protected def createStepFuture(
    newStepRequestFunc: (ThreadReference, Seq[JDIRequestArgument]) => Try[String],
    threadReference: ThreadReference,
    extraArguments: JDIArgument*
  ): Future[StepEventAndData] = {
    val tryPipeline = Try({
      val JDIArgumentGroup(rArgs, eArgs, _) =
        JDIArgumentGroup(extraArguments: _*)

      newStepRequestFunc(
        threadReference,
        rArgs :+ ThreadFilter(threadReference)
      ).get

      (threadReference, eArgs)
    }).map(newStepPipeline)

    tryPipelineToFuture(tryPipeline)
  }

  /**
   * Creates a new pipeline of step events. This is not memoized as step events
   * are one-per-thread and are closed after the pipeline's future is completed.
   *
   * @param args The additional event arguments to provide to the event handler
   *             feeding the new pipeline
   *
   * @return The new step event and data pipeline
   */
  protected def newStepPipeline(
    args: (ThreadReference, Seq[JDIEventArgument])
  ): IdentityPipeline[StepEventAndData] = {
    val newPipeline = eventManager
      .addEventDataStream(StepEventType, args._2: _*)
      .map(t => (t._1.asInstanceOf[StepEvent], t._2))
      .noop()

    newPipeline
  }

  /**
   * Converts the try of a pipeline into a future. If the pipeline was created,
   * convert it to a future. Otherwise, convert the failure to an immediate
   * failed future.
   *
   * @param result The attempted pipeline
   *
   * @return The future representing the attempted pipeline's
   */
  protected def tryPipelineToFuture(
    result: Try[IdentityPipeline[StepEventAndData]]
  ): Future[StepEventAndData] = result match {
    case Success(pipeline)  => pipeline.toFuture
    case Failure(error)     => Future.failed(error)
  }
}
