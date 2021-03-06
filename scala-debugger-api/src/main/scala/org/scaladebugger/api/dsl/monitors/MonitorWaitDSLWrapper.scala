package org.scaladebugger.api.dsl.monitors

import com.sun.jdi.event.MonitorWaitEvent
import org.scaladebugger.api.lowlevel.JDIArgument
import org.scaladebugger.api.lowlevel.events.data.JDIEventDataResult
import org.scaladebugger.api.pipelines.Pipeline.IdentityPipeline
import org.scaladebugger.api.profiles.traits.monitors.MonitorWaitProfile

import scala.util.Try

/**
 * Wraps a profile, providing DSL-like syntax.
 *
 * @param monitorWaitProfile The profile to wrap
 */
class MonitorWaitDSLWrapper private[dsl] (
  private val monitorWaitProfile: MonitorWaitProfile
) {
  /** Represents a MonitorWait event and any associated data. */
  type MonitorWaitEventAndData = (MonitorWaitEvent, Seq[JDIEventDataResult])

  /** @see MonitorWaitProfile#tryGetOrCreateMonitorWaitRequest(JDIArgument*) */
  def onMonitorWait(
    extraArguments: JDIArgument*
  ): Try[IdentityPipeline[MonitorWaitEvent]] =
    monitorWaitProfile.tryGetOrCreateMonitorWaitRequest(extraArguments: _*)

  /** @see MonitorWaitProfile#getOrCreateMonitorWaitRequest(JDIArgument*) */
  def onUnsafeMonitorWait(
    extraArguments: JDIArgument*
  ): IdentityPipeline[MonitorWaitEvent] =
    monitorWaitProfile.getOrCreateMonitorWaitRequest(extraArguments: _*)

  /** @see MonitorWaitProfile#getOrCreateMonitorWaitRequestWithData(JDIArgument*) */
  def onUnsafeMonitorWaitWithData(
    extraArguments: JDIArgument*
  ): IdentityPipeline[MonitorWaitEventAndData] =
    monitorWaitProfile.getOrCreateMonitorWaitRequestWithData(
      extraArguments: _*
    )

  /** @see MonitorWaitProfile#tryGetOrCreateMonitorWaitRequestWithData(JDIArgument*) */
  def onMonitorWaitWithData(
    extraArguments: JDIArgument*
  ): Try[IdentityPipeline[MonitorWaitEventAndData]] =
    monitorWaitProfile.tryGetOrCreateMonitorWaitRequestWithData(
      extraArguments: _*
    )
}
