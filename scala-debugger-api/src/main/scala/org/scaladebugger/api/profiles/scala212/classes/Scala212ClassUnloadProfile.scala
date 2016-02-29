package org.scaladebugger.api.profiles.scala212.classes
import acyclic.file

import org.scaladebugger.api.profiles.pure.classes.PureClassUnloadProfile

/**
 * Represents a profile for class unload events that adds logic
 * specifically for Scala 2.12.
 */
trait Scala212ClassUnloadProfile extends PureClassUnloadProfile
