package org.scaladebugger.api.profiles.dotty.classes
import acyclic.file

import org.scaladebugger.api.profiles.pure.classes.PureClassUnloadProfile

/**
 * Represents a profile for class unload events that adds logic
 * specifically for Scala's dotty compiler.
 */
trait DottyClassUnloadProfile extends PureClassUnloadProfile
