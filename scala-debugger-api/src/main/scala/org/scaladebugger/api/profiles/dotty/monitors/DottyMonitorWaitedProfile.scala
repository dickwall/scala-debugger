package org.scaladebugger.api.profiles.dotty.monitors
import acyclic.file

import org.scaladebugger.api.profiles.pure.monitors.PureMonitorWaitedProfile

/**
 * Represents a profile for monitor waited events that adds logic
 * specifically for Scala's dotty compiler.
 */
trait DottyMonitorWaitedProfile extends PureMonitorWaitedProfile

