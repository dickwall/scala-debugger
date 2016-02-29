package org.scaladebugger.api.profiles.traits.steps
import acyclic.file

import com.sun.jdi.ThreadReference
import com.sun.jdi.event.StepEvent
import org.scaladebugger.api.lowlevel.JDIArgument
import org.scaladebugger.api.lowlevel.events.data.JDIEventDataResult
import org.scaladebugger.api.lowlevel.steps.StepRequestInfo
import org.scaladebugger.api.pipelines.Pipeline.IdentityPipeline

import scala.concurrent.Future

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Try

/**
 * Represents the interface that needs to be implemented to provide
 * step functionality for a specific debug profile.
 */
trait StepProfile {
  /** Represents a step event and any associated data. */
  type StepEventAndData = (StepEvent, Seq[JDIEventDataResult])

  /**
   * Retrieves the collection of active and pending step requests.
   *
   * @return The collection of information on step requests
   */
  def stepRequests: Seq[StepRequestInfo]

  /**
   * Steps in from the current location to the next line.
   *
   * @param threadReference The thread in which to perform the step
   * @param extraArguments The additional JDI arguments to provide
   *
   * @return The resulting one-time event
   */
  def stepIntoLine(
    threadReference: ThreadReference,
    extraArguments: JDIArgument*
  ): Future[StepEvent] = {
    stepIntoLineWithData(threadReference, extraArguments: _*).map(_._1)
  }

  /**
   * Steps in from the current location to the next line.
   *
   * @param threadReference The thread in which to perform the step
   * @param extraArguments The additional JDI arguments to provide
   *
   * @return The resulting one-time event and any retrieved data based on
   *         requests from extra arguments
   */
  def stepIntoLineWithData(
    threadReference: ThreadReference,
    extraArguments: JDIArgument*
  ): Future[StepEventAndData]

  /**
   * Steps over from the current location to the next line.
   *
   * @param threadReference The thread in which to perform the step
   * @param extraArguments The additional JDI arguments to provide
   *
   * @return The resulting one-time event
   */
  def stepOverLine(
    threadReference: ThreadReference,
    extraArguments: JDIArgument*
  ): Future[StepEvent] = {
    stepOverLineWithData(threadReference, extraArguments: _*).map(_._1)
  }

  /**
   * Steps over from the current location to the next line.
   *
   * @param threadReference The thread in which to perform the step
   * @param extraArguments The additional JDI arguments to provide
   *
   * @return The resulting one-time event and any retrieved data based on
   *         requests from extra arguments
   */
  def stepOverLineWithData(
    threadReference: ThreadReference,
    extraArguments: JDIArgument*
  ): Future[StepEventAndData]

  /**
   * Steps out from the current location to the next line.
   *
   * @param threadReference The thread in which to perform the step
   * @param extraArguments The additional JDI arguments to provide
   *
   * @return The resulting one-time event
   */
  def stepOutLine(
    threadReference: ThreadReference,
    extraArguments: JDIArgument*
  ): Future[StepEvent] = {
    stepOutLineWithData(threadReference, extraArguments: _*).map(_._1)
  }

  /**
   * Steps out from the current location to the next line.
   *
   * @param threadReference The thread in which to perform the step
   * @param extraArguments The additional JDI arguments to provide
   *
   * @return The resulting one-time event and any retrieved data based on
   *         requests from extra arguments
   */
  def stepOutLineWithData(
    threadReference: ThreadReference,
    extraArguments: JDIArgument*
  ): Future[StepEventAndData]

  /**
   * Steps in from the current location to the next location.
   *
   * @param threadReference The thread in which to perform the step
   * @param extraArguments The additional JDI arguments to provide
   *
   * @return The resulting one-time event
   */
  def stepIntoMin(
    threadReference: ThreadReference,
    extraArguments: JDIArgument*
  ): Future[StepEvent] = {
    stepIntoMinWithData(threadReference, extraArguments: _*).map(_._1)
  }

  /**
   * Steps in from the current location to the next location.
   *
   * @param threadReference The thread in which to perform the step
   * @param extraArguments The additional JDI arguments to provide
   *
   * @return The resulting one-time event and any retrieved data based on
   *         requests from extra arguments
   */
  def stepIntoMinWithData(
    threadReference: ThreadReference,
    extraArguments: JDIArgument*
  ): Future[StepEventAndData]

  /**
   * Steps over from the current location to the next location.
   *
   * @param threadReference The thread in which to perform the step
   * @param extraArguments The additional JDI arguments to provide
   *
   * @return The resulting one-time event
   */
  def stepOverMin(
    threadReference: ThreadReference,
    extraArguments: JDIArgument*
  ): Future[StepEvent] = {
    stepOverMinWithData(threadReference, extraArguments: _*).map(_._1)
  }

  /**
   * Steps over from the current location to the next location.
   *
   * @param threadReference The thread in which to perform the step
   * @param extraArguments The additional JDI arguments to provide
   *
   * @return The resulting one-time event and any retrieved data based on
   *         requests from extra arguments
   */
  def stepOverMinWithData(
    threadReference: ThreadReference,
    extraArguments: JDIArgument*
  ): Future[StepEventAndData]

  /**
   * Steps out from the current location to the next location.
   *
   * @param threadReference The thread in which to perform the step
   * @param extraArguments The additional JDI arguments to provide
   *
   * @return The resulting one-time event
   */
  def stepOutMin(
    threadReference: ThreadReference,
    extraArguments: JDIArgument*
  ): Future[StepEvent] = {
    stepOutMinWithData(threadReference, extraArguments: _*).map(_._1)
  }

  /**
   * Steps out from the current location to the next location.
   *
   * @param threadReference The thread in which to perform the step
   * @param extraArguments The additional JDI arguments to provide
   *
   * @return The resulting one-time event and any retrieved data based on
   *         requests from extra arguments
   */
  def stepOutMinWithData(
    threadReference: ThreadReference,
    extraArguments: JDIArgument*
  ): Future[StepEventAndData]

  /**
   * Constructs a stream of step events.
   *
   * @param threadReference The thread with which to receive step events
   * @param extraArguments The additional JDI arguments to provide
   *
   * @return The stream of step events
   */
  def onStep(
    threadReference: ThreadReference,
    extraArguments: JDIArgument*
  ): Try[IdentityPipeline[StepEvent]] = {
    onStepWithData(threadReference, extraArguments: _*).map(_.map(_._1).noop())
  }

  /**
   * Constructs a stream of step events.
   *
   * @param threadReference The thread with which to receive step events
   * @param extraArguments The additional JDI arguments to provide
   *
   * @return The stream of step events and any retrieved data based on
   *         requests from extra arguments
   */
  def onStepWithData(
    threadReference: ThreadReference,
    extraArguments: JDIArgument*
  ): Try[IdentityPipeline[StepEventAndData]]

  /**
   * Constructs a stream of step events.
   *
   * @param threadReference The thread with which to receive step events
   * @param extraArguments The additional JDI arguments to provide
   *
   * @return The stream of step events
   */
  def onUnsafeStep(
    threadReference: ThreadReference,
    extraArguments: JDIArgument*
  ): IdentityPipeline[StepEvent] = {
    onStep(threadReference, extraArguments: _*).get
  }

  /**
   * Constructs a stream of step events.
   *
   * @param threadReference The thread with which to receive step events
   * @param extraArguments The additional JDI arguments to provide
   *
   * @return The stream of step events and any retrieved data based on
   *         requests from extra arguments
   */
  def onUnsafeStepWithData(
    threadReference: ThreadReference,
    extraArguments: JDIArgument*
  ): IdentityPipeline[StepEventAndData] = {
    onStepWithData(threadReference, extraArguments: _*).get
  }
}
