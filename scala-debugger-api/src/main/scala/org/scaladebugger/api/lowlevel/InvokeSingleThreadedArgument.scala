package org.scaladebugger.api.lowlevel
import acyclic.file

/**
 * Represents a setting to only resume the thread where the invocation of
 * the method will occur. This can result in deadlocks if other dependent
 * threads remain suspended.
 */
case object InvokeSingleThreadedArgument extends JDIArgument
