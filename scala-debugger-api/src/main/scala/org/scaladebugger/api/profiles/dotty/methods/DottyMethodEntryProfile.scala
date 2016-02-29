package org.scaladebugger.api.profiles.dotty.methods
import acyclic.file

import org.scaladebugger.api.profiles.pure.methods.PureMethodEntryProfile

/**
 * Represents a profile for method entry events that adds logic
 * specifically for Scala's dotty compiler.
 */
trait DottyMethodEntryProfile extends PureMethodEntryProfile

