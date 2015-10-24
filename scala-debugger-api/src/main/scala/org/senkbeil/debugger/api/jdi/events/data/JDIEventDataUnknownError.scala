package org.senkbeil.debugger.api.jdi.events.data

/**
 * Represents an unknown error that occurred when retrieving data.
 *
 * @param throwable The throwable representing the error that occurred
 */
case class JDIEventDataUnknownError(throwable: Throwable)
  extends JDIEventDataError
