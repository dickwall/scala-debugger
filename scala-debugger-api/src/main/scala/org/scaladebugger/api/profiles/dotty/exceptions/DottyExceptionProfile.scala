package org.scaladebugger.api.profiles.dotty.exceptions
import acyclic.file

import org.scaladebugger.api.profiles.pure.exceptions.PureExceptionProfile

/**
 * Represents a profile for exception events that adds logic
 * specifically for Scala's dotty compiler.
 */
trait DottyExceptionProfile extends PureExceptionProfile
