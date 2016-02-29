package org.scaladebugger.api.profiles.dotty.classes
import acyclic.file

import org.scaladebugger.api.profiles.pure.classes.PureClassPrepareProfile

/**
 * Represents a profile for class prepare events that adds logic
 * specifically for Scala's dotty compiler.
 */
trait DottyClassPrepareProfile extends PureClassPrepareProfile

