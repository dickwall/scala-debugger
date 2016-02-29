package org.scaladebugger.api.profiles.traits.monitors
import acyclic.file

import com.sun.jdi.event.MonitorWaitEvent
import org.scaladebugger.api.lowlevel.JDIArgument
import org.scaladebugger.api.lowlevel.events.data.JDIEventDataResult
import org.scaladebugger.api.lowlevel.monitors.MonitorWaitRequestInfo
import org.scaladebugger.api.pipelines.Pipeline
import org.scaladebugger.api.pipelines.Pipeline.IdentityPipeline

import scala.util.Try

/**
 * Represents the interface that needs to be implemented to provide
 * monitor wait functionality for a specific debug profile.
 */
trait MonitorWaitProfile {
  /** Represents a monitor wait event and any associated data. */
  type MonitorWaitEventAndData =
    (MonitorWaitEvent, Seq[JDIEventDataResult])

  /**
   * Retrieves the collection of active and pending monitor wait requests.
   *
   * @return The collection of information on monitor wait requests
   */
  def monitorWaitRequests: Seq[MonitorWaitRequestInfo]

  /**
   * Constructs a stream of monitor wait events.
   *
   * @param extraArguments The additional JDI arguments to provide
   *
   * @return The stream of monitor wait events
   */
  def onMonitorWait(
    extraArguments: JDIArgument*
  ): Try[IdentityPipeline[MonitorWaitEvent]] = {
    onMonitorWaitWithData(extraArguments: _*).map(_.map(_._1).noop())
  }

  /**
   * Constructs a stream of monitor wait events.
   *
   * @param extraArguments The additional JDI arguments to provide
   *
   * @return The stream of monitor wait events and any retrieved
   *         data based on requests from extra arguments
   */
  def onMonitorWaitWithData(
    extraArguments: JDIArgument*
  ): Try[IdentityPipeline[MonitorWaitEventAndData]]

  /**
   * Constructs a stream of monitor wait events.
   *
   * @param extraArguments The additional JDI arguments to provide
   *
   * @return The stream of monitor wait events
   */
  def onUnsafeMonitorWait(
    extraArguments: JDIArgument*
  ): IdentityPipeline[MonitorWaitEvent] = {
    onMonitorWait(extraArguments: _*).get
  }

  /**
   * Constructs a stream of monitor wait events.
   *
   * @param extraArguments The additional JDI arguments to provide
   *
   * @return The stream of monitor wait events and any retrieved
   *         data based on requests from extra arguments
   */
  def onUnsafeMonitorWaitWithData(
    extraArguments: JDIArgument*
  ): IdentityPipeline[MonitorWaitEventAndData] = {
    onMonitorWaitWithData(extraArguments: _*).get
  }
}
