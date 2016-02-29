package org.scaladebugger.api.profiles.scala210.monitors
import acyclic.file

import org.scaladebugger.api.profiles.pure.monitors.PureMonitorContendedEnteredProfile

/**
 * Represents a profile for monitor contended entered events that adds logic
 * specifically for Scala 2.10.
 */
trait Scala210MonitorContendedEnteredProfile extends PureMonitorContendedEnteredProfile

