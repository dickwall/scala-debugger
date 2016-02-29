package org.scaladebugger.api.profiles.traits.monitors
import acyclic.file

import com.sun.jdi.event.MonitorContendedEnterEvent
import org.scaladebugger.api.lowlevel.JDIArgument
import org.scaladebugger.api.lowlevel.events.data.JDIEventDataResult
import org.scaladebugger.api.lowlevel.monitors.MonitorContendedEnterRequestInfo
import org.scaladebugger.api.pipelines.Pipeline
import org.scaladebugger.api.pipelines.Pipeline.IdentityPipeline

import scala.util.Try

/**
 * Represents the interface that needs to be implemented to provide
 * monitor contended enter functionality for a specific debug profile.
 */
trait MonitorContendedEnterProfile {
  /** Represents a monitor contended enter event and any associated data. */
  type MonitorContendedEnterEventAndData =
    (MonitorContendedEnterEvent, Seq[JDIEventDataResult])

  /**
   * Retrieves the collection of active and pending monitor contended enter
   * requests.
   *
   * @return The collection of information on monitor contended enter requests
   */
  def monitorContendedEnterRequests: Seq[MonitorContendedEnterRequestInfo]

  /**
   * Constructs a stream of monitor contended enter events.
   *
   * @param extraArguments The additional JDI arguments to provide
   *
   * @return The stream of monitor contended enter events
   */
  def onMonitorContendedEnter(
    extraArguments: JDIArgument*
  ): Try[IdentityPipeline[MonitorContendedEnterEvent]] = {
    onMonitorContendedEnterWithData(extraArguments: _*).map(_.map(_._1).noop())
  }

  /**
   * Constructs a stream of monitor contended enter events.
   *
   * @param extraArguments The additional JDI arguments to provide
   *
   * @return The stream of monitor contended enter events and any retrieved
   *         data based on requests from extra arguments
   */
  def onMonitorContendedEnterWithData(
    extraArguments: JDIArgument*
  ): Try[IdentityPipeline[MonitorContendedEnterEventAndData]]

  /**
   * Constructs a stream of monitor contended enter events.
   *
   * @param extraArguments The additional JDI arguments to provide
   *
   * @return The stream of monitor contended enter events
   */
  def onUnsafeMonitorContendedEnter(
    extraArguments: JDIArgument*
  ): IdentityPipeline[MonitorContendedEnterEvent] = {
    onMonitorContendedEnter(extraArguments: _*).get
  }

  /**
   * Constructs a stream of monitor contended enter events.
   *
   * @param extraArguments The additional JDI arguments to provide
   *
   * @return The stream of monitor contended enter events and any retrieved
   *         data based on requests from extra arguments
   */
  def onUnsafeMonitorContendedEnterWithData(
    extraArguments: JDIArgument*
  ): IdentityPipeline[MonitorContendedEnterEventAndData] = {
    onMonitorContendedEnterWithData(extraArguments: _*).get
  }
}
