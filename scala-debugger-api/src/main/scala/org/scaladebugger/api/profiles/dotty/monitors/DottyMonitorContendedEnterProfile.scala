package org.scaladebugger.api.profiles.dotty.monitors
import acyclic.file

import org.scaladebugger.api.profiles.pure.monitors.PureMonitorContendedEnterProfile

/**
 * Represents a profile for monitor contended enter events that adds logic
 * specifically for Scala's dotty compiler.
 */
trait DottyMonitorContendedEnterProfile extends PureMonitorContendedEnterProfile

