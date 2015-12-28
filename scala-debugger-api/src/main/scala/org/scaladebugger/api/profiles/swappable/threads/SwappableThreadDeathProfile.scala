package org.scaladebugger.api.profiles.swappable.threads

import org.scaladebugger.api.lowlevel.JDIArgument
import org.scaladebugger.api.lowlevel.threads.ThreadDeathRequestInfo
import org.scaladebugger.api.pipelines.Pipeline.IdentityPipeline
import org.scaladebugger.api.profiles.swappable.SwappableDebugProfile
import org.scaladebugger.api.profiles.traits.threads.ThreadDeathProfile

import scala.util.Try

/**
 * Represents a swappable profile for thread death events that redirects the
 * invocation to another profile.
 */
trait SwappableThreadDeathProfile extends ThreadDeathProfile {
  this: SwappableDebugProfile =>

  override def onThreadDeathWithData(
    extraArguments: JDIArgument*
  ): Try[IdentityPipeline[ThreadDeathEventAndData]] = {
    withCurrentProfile.onThreadDeathWithData(extraArguments: _*)
  }

  override def threadDeathRequests: Seq[ThreadDeathRequestInfo] = {
    withCurrentProfile.threadDeathRequests
  }
}
