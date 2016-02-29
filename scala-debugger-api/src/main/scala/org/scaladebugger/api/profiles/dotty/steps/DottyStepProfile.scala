package org.scaladebugger.api.profiles.dotty.steps
import acyclic.file

import org.scaladebugger.api.profiles.pure.steps.PureStepProfile

/**
 * Represents a profile for step events that adds logic
 * specifically for Scala's dotty compiler.
 */
trait DottyStepProfile extends PureStepProfile

