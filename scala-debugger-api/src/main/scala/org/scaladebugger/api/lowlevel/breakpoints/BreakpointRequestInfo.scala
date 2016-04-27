package org.scaladebugger.api.lowlevel.breakpoints
import acyclic.file

import org.scaladebugger.api.lowlevel.RequestInfo
import org.scaladebugger.api.lowlevel.requests.JDIRequestArgument

/**
 * Represents information about a breakpoint request.
 *
 * @param requestId The id of the request
 * @param isPending Whether or not this request is pending (not on remote JVM)
 * @param isShortFileName Whether or not the file name represents a short file
 *                        name (file.scala) or the full source path
 * @param fileName The name of the file containing the breakpoint
 * @param lineNumber The number of the line where the breakpoint is set
 * @param extraArguments The additional arguments provided to the breakpoint
 */
case class BreakpointRequestInfo(
  requestId: String,
  isPending: Boolean,
  isShortFileName: Boolean,
  fileName: String,
  lineNumber: Int,
  extraArguments: Seq[JDIRequestArgument] = Nil
) extends RequestInfo

