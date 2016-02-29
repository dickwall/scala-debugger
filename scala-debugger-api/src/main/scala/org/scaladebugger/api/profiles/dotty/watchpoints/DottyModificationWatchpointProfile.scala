package org.scaladebugger.api.profiles.dotty.watchpoints
import acyclic.file

import org.scaladebugger.api.profiles.pure.watchpoints.PureModificationWatchpointProfile

/**
 * Represents a profile for modification watchpoint events that adds logic
 * specifically for Scala's dotty compiler.
 */
trait DottyModificationWatchpointProfile extends PureModificationWatchpointProfile

