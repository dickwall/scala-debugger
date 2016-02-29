package org.scaladebugger.api.profiles.traits.classes
import acyclic.file

import com.sun.jdi.event.ClassUnloadEvent
import org.scaladebugger.api.lowlevel.JDIArgument
import org.scaladebugger.api.lowlevel.classes.ClassUnloadRequestInfo
import org.scaladebugger.api.lowlevel.events.data.JDIEventDataResult
import org.scaladebugger.api.pipelines.Pipeline.IdentityPipeline

import scala.util.Try

/**
 * Represents the interface that needs to be implemented to provide
 * class unload functionality for a specific debug profile.
 */
trait ClassUnloadProfile {
  /** Represents a class unload event and any associated data. */
  type ClassUnloadEventAndData = (ClassUnloadEvent, Seq[JDIEventDataResult])

  /**
   * Retrieves the collection of active and pending class unload requests.
   *
   * @return The collection of information on class unload requests
   */
  def classUnloadRequests: Seq[ClassUnloadRequestInfo]

  /**
   * Constructs a stream of class unload events.
   *
   * @param extraArguments The additional JDI arguments to provide
   *
   * @return The stream of class unload events
   */
  def onClassUnload(
    extraArguments: JDIArgument*
  ): Try[IdentityPipeline[ClassUnloadEvent]] = {
    onClassUnloadWithData(extraArguments: _*).map(_.map(_._1).noop())
  }

  /**
   * Constructs a stream of class unload events.
   *
   * @param extraArguments The additional JDI arguments to provide
   *
   * @return The stream of class unload events
   */
  def onUnsafeClassUnload(
    extraArguments: JDIArgument*
  ): IdentityPipeline[ClassUnloadEvent] = {
    onClassUnload(extraArguments: _*).get
  }

  /**
   * Constructs a stream of class unload events.
   *
   * @param extraArguments The additional JDI arguments to provide
   *
   * @return The stream of class unload events and any retrieved data based on
   *         requests from extra arguments
   */
  def onUnsafeClassUnloadWithData(
    extraArguments: JDIArgument*
  ): IdentityPipeline[ClassUnloadEventAndData] = {
    onClassUnloadWithData(extraArguments: _*).get
  }

  /**
   * Constructs a stream of class unload events.
   *
   * @param extraArguments The additional JDI arguments to provide
   *
   * @return The stream of class unload events and any retrieved data based on
   *         requests from extra arguments
   */
  def onClassUnloadWithData(
    extraArguments: JDIArgument*
  ): Try[IdentityPipeline[ClassUnloadEventAndData]]
}
