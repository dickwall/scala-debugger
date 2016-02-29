package org.scaladebugger.api.profiles.dotty.events
import acyclic.file

import org.scaladebugger.api.profiles.pure.events.PureEventProfile

/**
 * Represents a profile for events that adds logic specifically for Scala's dotty compiler.
 */
trait DottyEventProfile extends PureEventProfile
