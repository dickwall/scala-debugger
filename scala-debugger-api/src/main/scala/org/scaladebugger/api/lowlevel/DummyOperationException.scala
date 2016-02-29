package org.scaladebugger.api.lowlevel
import acyclic.file

/**
 * Represents an exception that is thrown by dummy managers (normally as the
 * exception in a Failure return value).
 */
class DummyOperationException extends Exception
