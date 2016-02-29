package org.scaladebugger.api.profiles.scala211.classes
import acyclic.file

import org.scaladebugger.api.profiles.pure.classes.PureClassUnloadProfile

/**
 * Represents a profile for class unload events that adds logic
 * specifically for Scala 2.11.
 */
trait Scala211ClassUnloadProfile extends PureClassUnloadProfile
