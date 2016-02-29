package org.scaladebugger.api.lowlevel.breakpoints
import acyclic.file

import org.scaladebugger.api.lowlevel.RequestInfo
import org.scaladebugger.api.lowlevel.requests.JDIRequestArgument

/**
 * Represents information about a breakpoint request.
 *
 * @param requestId The id of the request
 * @param fileName The name of the file containing the breakpoint
 * @param lineNumber The number of the line where the breakpoint is set
 * @param extraArguments The additional arguments provided to the breakpoint
 */
case class BreakpointRequestInfo(
  requestId: String,
  fileName: String,
  lineNumber: Int,
  extraArguments: Seq[JDIRequestArgument] = Nil
) extends RequestInfo

