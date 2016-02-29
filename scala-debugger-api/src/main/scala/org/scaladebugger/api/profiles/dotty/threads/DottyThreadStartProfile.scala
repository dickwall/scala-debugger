package org.scaladebugger.api.profiles.dotty.threads
import acyclic.file

import org.scaladebugger.api.profiles.pure.threads.PureThreadStartProfile

/**
 * Represents a profile for thread start events that adds logic
 * specifically for Scala's dotty compiler.
 */
trait DottyThreadStartProfile extends PureThreadStartProfile

