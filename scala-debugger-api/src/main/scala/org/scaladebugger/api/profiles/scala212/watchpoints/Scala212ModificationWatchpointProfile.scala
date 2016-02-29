package org.scaladebugger.api.profiles.scala212.watchpoints
import acyclic.file

import org.scaladebugger.api.profiles.pure.watchpoints.PureModificationWatchpointProfile

/**
 * Represents a profile for modification watchpoint events that adds logic
 * specifically for Scala 2.12.
 */
trait Scala212ModificationWatchpointProfile extends PureModificationWatchpointProfile

