package org.scaladebugger.api.lowlevel.methods
import acyclic.file

import org.scaladebugger.api.lowlevel.RequestInfo
import org.scaladebugger.api.lowlevel.requests.JDIRequestArgument

/**
 * Represents information about a method entry request.
 *
 * @param requestId The id of the request
 * @param isPending Whether or not this request is pending (not on remote JVM)
 * @param className The full name of the class containing the method
 * @param methodName The name of the method
 * @param extraArguments The additional arguments provided to the
 *                       method entry request
 */
case class MethodEntryRequestInfo(
  requestId: String,
  isPending: Boolean,
  className: String,
  methodName: String,
  extraArguments: Seq[JDIRequestArgument] = Nil
) extends RequestInfo

