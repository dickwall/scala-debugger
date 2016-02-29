package org.scaladebugger.api.profiles.dotty.info
import acyclic.file

import org.scaladebugger.api.profiles.pure.info.PureGrabInfoProfile

/**
 * Represents a profile for grabbing various info that adds logic
 * specifically for Scala's dotty compiler.
 */
trait DottyGrabInfoProfile extends PureGrabInfoProfile

