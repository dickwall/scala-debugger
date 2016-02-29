package org.scaladebugger.api.profiles.scala211.watchpoints
import acyclic.file

import org.scaladebugger.api.profiles.pure.watchpoints.PureAccessWatchpointProfile

/**
 * Represents a profile for access watchpoint events that adds logic
 * specifically for Scala 2.11.
 */
trait Scala211AccessWatchpointProfile extends PureAccessWatchpointProfile

