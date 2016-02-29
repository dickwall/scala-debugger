package org.scaladebugger.api.profiles.scala210.monitors
import acyclic.file

import org.scaladebugger.api.profiles.pure.monitors.PureMonitorContendedEnterProfile

/**
 * Represents a profile for monitor contended enter events that adds logic
 * specifically for Scala 2.10.
 */
trait Scala210MonitorContendedEnterProfile extends PureMonitorContendedEnterProfile

