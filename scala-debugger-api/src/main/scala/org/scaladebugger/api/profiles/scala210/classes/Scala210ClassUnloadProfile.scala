package org.scaladebugger.api.profiles.scala210.classes
import acyclic.file

import org.scaladebugger.api.profiles.pure.classes.PureClassUnloadProfile

/**
 * Represents a profile for class unload events that adds logic
 * specifically for Scala 2.10.
 */
trait Scala210ClassUnloadProfile extends PureClassUnloadProfile
