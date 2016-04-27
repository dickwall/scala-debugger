package org.scaladebugger.api.lowlevel.requests.misc.processors

//import acyclic.file

import com.sun.jdi.request.EventRequest
import org.scaladebugger.api.lowlevel.requests.misc.ShortName
import org.scaladebugger.api.lowlevel.requests.{JDIRequestArgument, JDIRequestProcessor}

/**
 * Represents a processor for short names.
 *
 * @param shortName The short name entity to use when processing
 */
class ShortNameProcessor(
  val shortName: ShortName
) extends JDIRequestProcessor {
  /**
   * Processes the provided event request.
   *
   * @param eventRequest The request to process
   * @return The unchanged event request
   */
  override def process(eventRequest: EventRequest): EventRequest = eventRequest

  override val argument: JDIRequestArgument = shortName
}
