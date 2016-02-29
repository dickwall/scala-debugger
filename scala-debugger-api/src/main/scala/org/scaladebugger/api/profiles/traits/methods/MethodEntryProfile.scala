package org.scaladebugger.api.profiles.traits.methods
import acyclic.file

import com.sun.jdi.event.MethodEntryEvent
import org.scaladebugger.api.lowlevel.JDIArgument
import org.scaladebugger.api.lowlevel.events.data.JDIEventDataResult
import org.scaladebugger.api.lowlevel.methods.MethodEntryRequestInfo
import org.scaladebugger.api.pipelines.Pipeline
import org.scaladebugger.api.pipelines.Pipeline.IdentityPipeline

import scala.util.Try

/**
 * Represents the interface that needs to be implemented to provide
 * method entry functionality for a specific debug profile.
 */
trait MethodEntryProfile {
  /** Represents a method entry event and any associated data. */
  type MethodEntryEventAndData = (MethodEntryEvent, Seq[JDIEventDataResult])

  /**
   * Retrieves the collection of active and pending method entry requests.
   *
   * @return The collection of information on method entry requests
   */
  def methodEntryRequests: Seq[MethodEntryRequestInfo]

  /**
   * Constructs a stream of method entry events for the specified class and
   * method.
   *
   * @param className The full name of the class/object/trait containing the
   *                  method to watch
   * @param methodName The name of the method to watch
   * @param extraArguments The additional JDI arguments to provide
   *
   * @return The stream of method entry events
   */
  def onMethodEntry(
    className: String,
    methodName: String,
    extraArguments: JDIArgument*
  ): Try[IdentityPipeline[MethodEntryEvent]] = {
    onMethodEntryWithData(
      className: String,
      methodName: String,
      extraArguments: _*
    ).map(_.map(_._1).noop())
  }

  /**
   * Constructs a stream of method entry events for the specified class and
   * method.
   *
   * @param className The full name of the class/object/trait containing the
   *                  method to watch
   * @param methodName The name of the method to watch
   * @param extraArguments The additional JDI arguments to provide
   *
   * @return The stream of method entry events
   */
  def onUnsafeMethodEntry(
    className: String,
    methodName: String,
    extraArguments: JDIArgument*
  ): IdentityPipeline[MethodEntryEvent] = {
    onMethodEntry(
      className,
      methodName,
      extraArguments: _*
    ).get
  }

  /**
   * Constructs a stream of method entry events for the specified class and
   * method.
   *
   * @param className The full name of the class/object/trait containing the
   *                  method to watch
   * @param methodName The name of the method to watch
   * @param extraArguments The additional JDI arguments to provide
   *
   * @return The stream of method entry events and any retrieved data based on
   *         requests from extra arguments
   */
  def onUnsafeMethodEntryWithData(
    className: String,
    methodName: String,
    extraArguments: JDIArgument*
  ): IdentityPipeline[MethodEntryEventAndData] = {
    onMethodEntryWithData(
      className,
      methodName,
      extraArguments: _*
    ).get
  }

  /**
   * Constructs a stream of method entry events for the specified class and
   * method.
   *
   * @param className The full name of the class/object/trait containing the
   *                  method to watch
   * @param methodName The name of the method to watch
   * @param extraArguments The additional JDI arguments to provide
   *
   * @return The stream of method entry events and any retrieved data based on
   *         requests from extra arguments
   */
  def onMethodEntryWithData(
    className: String,
    methodName: String,
    extraArguments: JDIArgument*
  ): Try[IdentityPipeline[MethodEntryEventAndData]]
}
