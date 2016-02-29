package org.scaladebugger.api.profiles.traits.monitors
import acyclic.file

import com.sun.jdi.event.MonitorContendedEnteredEvent
import org.scaladebugger.api.lowlevel.JDIArgument
import org.scaladebugger.api.lowlevel.events.data.JDIEventDataResult
import org.scaladebugger.api.lowlevel.monitors.MonitorContendedEnteredRequestInfo
import org.scaladebugger.api.pipelines.Pipeline
import org.scaladebugger.api.pipelines.Pipeline.IdentityPipeline

import scala.util.Try

/**
 * Represents the interface that needs to be implemented to provide
 * monitor contended entered functionality for a specific debug profile.
 */
trait MonitorContendedEnteredProfile {
  /** Represents a monitor contended entered event and any associated data. */
  type MonitorContendedEnteredEventAndData =
    (MonitorContendedEnteredEvent, Seq[JDIEventDataResult])

  /**
   * Retrieves the collection of active and pending monitor contended entered
   * requests.
   *
   * @return The collection of information on monitor contended entered requests
   */
  def monitorContendedEnteredRequests: Seq[MonitorContendedEnteredRequestInfo]

  /**
   * Constructs a stream of monitor contended entered events.
   *
   * @param extraArguments The additional JDI arguments to provide
   *
   * @return The stream of monitor contended entered events
   */
  def onMonitorContendedEntered(
    extraArguments: JDIArgument*
  ): Try[IdentityPipeline[MonitorContendedEnteredEvent]] = {
    onMonitorContendedEnteredWithData(extraArguments: _*)
      .map(_.map(_._1).noop())
  }

  /**
   * Constructs a stream of monitor contended entered events.
   *
   * @param extraArguments The additional JDI arguments to provide
   *
   * @return The stream of monitor contended entered events and any retrieved
   *         data based on requests from extra arguments
   */
  def onMonitorContendedEnteredWithData(
    extraArguments: JDIArgument*
  ): Try[IdentityPipeline[MonitorContendedEnteredEventAndData]]

  /**
   * Constructs a stream of monitor contended entered events.
   *
   * @param extraArguments The additional JDI arguments to provide
   *
   * @return The stream of monitor contended entered events
   */
  def onUnsafeMonitorContendedEntered(
    extraArguments: JDIArgument*
  ): IdentityPipeline[MonitorContendedEnteredEvent] = {
    onMonitorContendedEntered(extraArguments: _*).get
  }

  /**
   * Constructs a stream of monitor contended entered events.
   *
   * @param extraArguments The additional JDI arguments to provide
   *
   * @return The stream of monitor contended entered events and any retrieved
   *         data based on requests from extra arguments
   */
  def onUnsafeMonitorContendedEnteredWithData(
    extraArguments: JDIArgument*
  ): IdentityPipeline[MonitorContendedEnteredEventAndData] = {
    onMonitorContendedEnteredWithData(extraArguments: _*).get
  }
}
