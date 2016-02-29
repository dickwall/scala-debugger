package org.scaladebugger.api.profiles.swappable.threads
import acyclic.file

import org.scaladebugger.api.lowlevel.JDIArgument
import org.scaladebugger.api.lowlevel.threads.ThreadStartRequestInfo
import org.scaladebugger.api.pipelines.Pipeline.IdentityPipeline
import org.scaladebugger.api.profiles.swappable.SwappableDebugProfileManagement
import org.scaladebugger.api.profiles.traits.threads.ThreadStartProfile

import scala.util.Try

/**
 * Represents a swappable profile for thread start events that redirects the
 * invocation to another profile.
 */
trait SwappableThreadStartProfile extends ThreadStartProfile {
  this: SwappableDebugProfileManagement =>

  override def onThreadStartWithData(
    extraArguments: JDIArgument*
  ): Try[IdentityPipeline[ThreadStartEventAndData]] = {
    withCurrentProfile.onThreadStartWithData(extraArguments: _*)
  }

  override def threadStartRequests: Seq[ThreadStartRequestInfo] = {
    withCurrentProfile.threadStartRequests
  }
}
