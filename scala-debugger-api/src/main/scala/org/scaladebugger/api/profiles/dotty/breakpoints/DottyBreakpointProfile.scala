package org.scaladebugger.api.profiles.dotty.breakpoints

import org.scaladebugger.api.profiles.pure.breakpoints.PureBreakpointProfile

/**
 * Represents a profile for breakpoints that adds logic specifically for
 * Scala's dotty compiler.
 */
trait DottyBreakpointProfile extends PureBreakpointProfile
