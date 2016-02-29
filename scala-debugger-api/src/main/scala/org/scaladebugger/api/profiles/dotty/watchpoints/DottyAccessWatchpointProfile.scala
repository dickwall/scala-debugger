package org.scaladebugger.api.profiles.dotty.watchpoints
import acyclic.file

import org.scaladebugger.api.profiles.pure.watchpoints.PureAccessWatchpointProfile

/**
 * Represents a profile for access watchpoint events that adds logic
 * specifically for Scala's dotty compiler.
 */
trait DottyAccessWatchpointProfile extends PureAccessWatchpointProfile

