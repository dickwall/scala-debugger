package org.scaladebugger.api.profiles.dotty.monitors
import acyclic.file

import org.scaladebugger.api.profiles.pure.monitors.PureMonitorWaitProfile

/**
 * Represents a profile for monitor wait events that adds logic
 * specifically for Scala's dotty compiler.
 */
trait DottyMonitorWaitProfile extends PureMonitorWaitProfile

