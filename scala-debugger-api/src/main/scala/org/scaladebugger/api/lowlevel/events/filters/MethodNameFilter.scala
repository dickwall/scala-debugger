package org.scaladebugger.api.lowlevel.events.filters
//import acyclic.file

import org.scaladebugger.api.lowlevel.events.filters.processors.MethodNameFilterProcessor

/**
 * Represents a local filter that will result in ignoring any incoming event if
 * it does not have the specified method name.
 *
 * @example MethodNameFilter(name = "someMethod") will ignore any event not
 *          originating from this method.
 *
 * @note Only valid for MethodEntry, MethodExit, and Locatable events. All
 *       other events are ignored and do not affect the overall filter results.
 *
 * @param name The name of the method
 */
case class MethodNameFilter(name: String) extends JDIEventFilter {
  /**
   * Creates a new JDI event processor based on this filter.
   *
   * @return The new JDI event filter processor instance
   */
  override def toProcessor: JDIEventFilterProcessor =
    new MethodNameFilterProcessor(this)
}
