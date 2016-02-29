package org.scaladebugger.api.profiles.scala212.watchpoints
import acyclic.file

import org.scaladebugger.api.profiles.pure.watchpoints.PureAccessWatchpointProfile

/**
 * Represents a profile for access watchpoint events that adds logic
 * specifically for Scala 2.12.
 */
trait Scala212AccessWatchpointProfile extends PureAccessWatchpointProfile

