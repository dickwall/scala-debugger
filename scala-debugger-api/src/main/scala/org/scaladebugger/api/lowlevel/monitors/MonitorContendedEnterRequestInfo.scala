package org.scaladebugger.api.lowlevel.monitors
import acyclic.file

import org.scaladebugger.api.lowlevel.RequestInfo
import org.scaladebugger.api.lowlevel.requests.JDIRequestArgument

/**
 * Represents information about a monitor contended enter request.
 *
 * @param requestId The id of the request
 * @param extraArguments The additional arguments provided to the
 *                       monitor contended enter request
 */
case class MonitorContendedEnterRequestInfo(
  requestId: String,
  extraArguments: Seq[JDIRequestArgument] = Nil
) extends RequestInfo

