package org.scaladebugger.api.profiles.dotty.threads
import acyclic.file

import org.scaladebugger.api.profiles.pure.threads.PureThreadDeathProfile

/**
 * Represents a profile for thread death events that adds logic
 * specifically for Scala's dotty compiler.
 */
trait DottyThreadDeathProfile extends PureThreadDeathProfile

