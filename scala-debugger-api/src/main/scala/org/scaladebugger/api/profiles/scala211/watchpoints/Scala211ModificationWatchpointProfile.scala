package org.scaladebugger.api.profiles.scala211.watchpoints
import acyclic.file

import org.scaladebugger.api.profiles.pure.watchpoints.PureModificationWatchpointProfile

/**
 * Represents a profile for modification watchpoint events that adds logic
 * specifically for Scala 2.11.
 */
trait Scala211ModificationWatchpointProfile extends PureModificationWatchpointProfile

