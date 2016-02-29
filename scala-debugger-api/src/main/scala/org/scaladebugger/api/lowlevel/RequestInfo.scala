package org.scaladebugger.api.lowlevel
import acyclic.file

/**
 * Represents generic information about a request.
 */
trait RequestInfo {
  /** The id of the request. */
  val requestId: String
}
