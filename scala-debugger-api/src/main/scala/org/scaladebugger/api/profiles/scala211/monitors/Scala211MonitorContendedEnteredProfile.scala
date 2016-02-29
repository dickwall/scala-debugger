package org.scaladebugger.api.profiles.scala211.monitors
import acyclic.file

import org.scaladebugger.api.profiles.pure.monitors.PureMonitorContendedEnteredProfile

/**
 * Represents a profile for monitor contended entered events that adds logic
 * specifically for Scala 2.11.
 */
trait Scala211MonitorContendedEnteredProfile extends PureMonitorContendedEnteredProfile

