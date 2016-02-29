package org.scaladebugger.api.profiles.traits.breakpoints
import acyclic.file

import com.sun.jdi.event.BreakpointEvent
import org.scaladebugger.api.lowlevel.JDIArgument
import org.scaladebugger.api.lowlevel.breakpoints.BreakpointRequestInfo
import org.scaladebugger.api.lowlevel.events.data.JDIEventDataResult
import org.scaladebugger.api.pipelines.Pipeline
import org.scaladebugger.api.pipelines.Pipeline.IdentityPipeline

import scala.util.Try

/**
 * Represents the interface that needs to be implemented to provide
 * breakpoint functionality for a specific debug profile.
 */
trait BreakpointProfile {
  /** Represents a breakpoint event and any associated data. */
  type BreakpointEventAndData = (BreakpointEvent, Seq[JDIEventDataResult])

  /**
   * Retrieves the collection of active and pending breakpoints requests.
   *
   * @return The collection of information on breakpoint requests
   */
  def breakpointRequests: Seq[BreakpointRequestInfo]

  /**
   * Constructs a stream of breakpoint events for the specified file and line
   * number.
   *
   * @param fileName The name of the file where the breakpoint will be set
   * @param lineNumber The line number within the file where the breakpoint
   *                   will be set
   * @param extraArguments The additional JDI arguments to provide
   *
   * @return The stream of breakpoint events
   */
  def onBreakpoint(
    fileName: String,
    lineNumber: Int,
    extraArguments: JDIArgument*
  ): Try[IdentityPipeline[BreakpointEvent]] = {
    onBreakpointWithData(
      fileName,
      lineNumber,
      extraArguments: _*
    ).map(_.map(_._1).noop())
  }

  /**
   * Constructs a stream of breakpoint events for the specified file and line
   * number.
   *
   * @param fileName The name of the file where the breakpoint will be set
   * @param lineNumber The line number within the file where the breakpoint
   *                   will be set
   * @param extraArguments The additional JDI arguments to provide
   *
   * @return The stream of breakpoint events
   */
  def onUnsafeBreakpoint(
    fileName: String,
    lineNumber: Int,
    extraArguments: JDIArgument*
  ): IdentityPipeline[BreakpointEvent] = {
    onBreakpoint(
      fileName,
      lineNumber,
      extraArguments: _*
    ).get
  }

  /**
   * Constructs a stream of breakpoint events for the specified file and line
   * number.
   *
   * @param fileName The name of the file where the breakpoint will be set
   * @param lineNumber The line number within the file where the breakpoint
   *                   will be set
   * @param extraArguments The additional JDI arguments to provide
   *
   * @return The stream of breakpoint events and any retrieved data based on
   *         requests from extra arguments
   */
  def onUnsafeBreakpointWithData(
    fileName: String,
    lineNumber: Int,
    extraArguments: JDIArgument*
  ): IdentityPipeline[BreakpointEventAndData] = {
    onBreakpointWithData(
      fileName,
      lineNumber,
      extraArguments: _*
    ).get
  }

  /**
   * Constructs a stream of breakpoint events for the specified file and line
   * number.
   *
   * @param fileName The name of the file where the breakpoint will be set
   * @param lineNumber The line number within the file where the breakpoint
   *                   will be set
   * @param extraArguments The additional JDI arguments to provide
   *
   * @return The stream of breakpoint events and any retrieved data based on
   *         requests from extra arguments
   */
  def onBreakpointWithData(
    fileName: String,
    lineNumber: Int,
    extraArguments: JDIArgument*
  ): Try[IdentityPipeline[BreakpointEventAndData]]
}
