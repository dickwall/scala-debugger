package org.scaladebugger.api.profiles.swappable.monitors
import acyclic.file

import org.scaladebugger.api.lowlevel.JDIArgument
import org.scaladebugger.api.lowlevel.monitors.MonitorContendedEnteredRequestInfo
import org.scaladebugger.api.pipelines.Pipeline.IdentityPipeline
import org.scaladebugger.api.profiles.swappable.SwappableDebugProfileManagement
import org.scaladebugger.api.profiles.traits.monitors.MonitorContendedEnteredProfile

import scala.util.Try

/**
 * Represents a swappable profile for monitor contended entered events that
 * redirects the invocation to another profile.
 */
trait SwappableMonitorContendedEnteredProfile extends MonitorContendedEnteredProfile {
  this: SwappableDebugProfileManagement =>

  override def onMonitorContendedEnteredWithData(
    extraArguments: JDIArgument*
  ): Try[IdentityPipeline[MonitorContendedEnteredEventAndData]] = {
    withCurrentProfile.onMonitorContendedEnteredWithData(extraArguments: _*)
  }

  override def monitorContendedEnteredRequests: Seq[MonitorContendedEnteredRequestInfo] = {
    withCurrentProfile.monitorContendedEnteredRequests
  }
}
