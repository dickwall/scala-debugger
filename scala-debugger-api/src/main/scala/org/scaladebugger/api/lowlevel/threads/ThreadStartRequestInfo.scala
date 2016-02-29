package org.scaladebugger.api.lowlevel.threads
import acyclic.file

import org.scaladebugger.api.lowlevel.RequestInfo
import org.scaladebugger.api.lowlevel.requests.JDIRequestArgument

/**
 * Represents information about a thread start request.
 *
 * @param requestId The id of the request
 * @param extraArguments The additional arguments provided to the
 *                       thread start request
 */
case class ThreadStartRequestInfo(
  requestId: String,
  extraArguments: Seq[JDIRequestArgument] = Nil
) extends RequestInfo

