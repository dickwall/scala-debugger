package org.scaladebugger.api.profiles.scala211.threads
import acyclic.file

import org.scaladebugger.api.profiles.pure.threads.PureThreadDeathProfile

/**
 * Represents a profile for thread death events that adds logic
 * specifically for Scala 2.11.
 */
trait Scala211ThreadDeathProfile extends PureThreadDeathProfile

