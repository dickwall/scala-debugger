package org.scaladebugger.api.profiles.swappable.monitors

import org.scaladebugger.api.lowlevel.JDIArgument
import org.scaladebugger.api.lowlevel.monitors.MonitorWaitedRequestInfo
import org.scaladebugger.api.pipelines.Pipeline.IdentityPipeline
import org.scaladebugger.api.profiles.swappable.SwappableDebugProfile
import org.scaladebugger.api.profiles.traits.monitors.MonitorWaitedProfile

import scala.util.Try

/**
 * Represents a swappable profile for monitor waited events that
 * redirects the invocation to another profile.
 */
trait SwappableMonitorWaitedProfile extends MonitorWaitedProfile {
  this: SwappableDebugProfile =>

  override def onMonitorWaitedWithData(
    extraArguments: JDIArgument*
  ): Try[IdentityPipeline[MonitorWaitedEventAndData]] = {
    withCurrentProfile.onMonitorWaitedWithData(extraArguments: _*)
  }

  override def monitorWaitedRequests: Seq[MonitorWaitedRequestInfo] = {
    withCurrentProfile.monitorWaitedRequests
  }
}
