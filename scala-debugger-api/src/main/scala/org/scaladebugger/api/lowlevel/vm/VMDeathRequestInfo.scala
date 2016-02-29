package org.scaladebugger.api.lowlevel.vm
import acyclic.file

import org.scaladebugger.api.lowlevel.RequestInfo
import org.scaladebugger.api.lowlevel.requests.JDIRequestArgument

/**
 * Represents information about a vm death request.
 *
 * @param requestId The id of the request
 * @param extraArguments The additional arguments provided to the
 *                       vm death request
 */
case class VMDeathRequestInfo(
  requestId: String,
  extraArguments: Seq[JDIRequestArgument] = Nil
) extends RequestInfo

