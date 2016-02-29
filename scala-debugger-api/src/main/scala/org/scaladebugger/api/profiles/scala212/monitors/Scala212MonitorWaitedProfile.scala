package org.scaladebugger.api.profiles.scala212.monitors
import acyclic.file

import org.scaladebugger.api.profiles.pure.monitors.PureMonitorWaitedProfile

/**
 * Represents a profile for monitor waited events that adds logic
 * specifically for Scala 2.12.
 */
trait Scala212MonitorWaitedProfile extends PureMonitorWaitedProfile

