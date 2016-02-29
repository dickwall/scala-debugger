package org.scaladebugger.api.profiles.dotty.monitors
import acyclic.file

import org.scaladebugger.api.profiles.pure.monitors.PureMonitorContendedEnteredProfile

/**
 * Represents a profile for monitor contended entered events that adds logic
 * specifically for Scala's dotty compiler.
 */
trait DottyMonitorContendedEnteredProfile extends PureMonitorContendedEnteredProfile

