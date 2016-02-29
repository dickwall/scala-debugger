package org.scaladebugger.api.profiles.swappable.breakpoints
import acyclic.file

import org.scaladebugger.api.lowlevel.JDIArgument
import org.scaladebugger.api.lowlevel.breakpoints.BreakpointRequestInfo
import org.scaladebugger.api.pipelines.Pipeline.IdentityPipeline
import org.scaladebugger.api.profiles.swappable.SwappableDebugProfileManagement
import org.scaladebugger.api.profiles.traits.breakpoints.BreakpointProfile

import scala.util.Try

/**
 * Represents a swappable profile for breakpoints that redirects the invocation
 * to another profile.
 */
trait SwappableBreakpointProfile extends BreakpointProfile {
  this: SwappableDebugProfileManagement =>

  override def onBreakpointWithData(
    fileName: String,
    lineNumber: Int,
    extraArguments: JDIArgument*
  ): Try[IdentityPipeline[BreakpointEventAndData]] = {
    withCurrentProfile.onBreakpointWithData(
      fileName,
      lineNumber,
      extraArguments: _*
    )
  }

  override def breakpointRequests: Seq[BreakpointRequestInfo] = {
    withCurrentProfile.breakpointRequests
  }
}
