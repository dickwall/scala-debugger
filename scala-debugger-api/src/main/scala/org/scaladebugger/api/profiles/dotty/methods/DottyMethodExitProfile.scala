package org.scaladebugger.api.profiles.dotty.methods
import acyclic.file

import org.scaladebugger.api.profiles.pure.methods.PureMethodExitProfile

/**
 * Represents a profile for method exit events that adds logic
 * specifically for Scala's dotty compiler.
 */
trait DottyMethodExitProfile extends PureMethodExitProfile
