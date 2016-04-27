package org.scaladebugger.api.lowlevel.breakpoints

/**
 * Represents an exception that occurred when attempting to create a breakpoint
 * request using a short filename (file.scala) and unable to find any matching
 * source path (org/potato/file.scala).
 *
 * @param fileName The name of the file where the breakpoint was attempted
 */
class NoSourcePathFoundException(fileName: String)
  extends Exception(s"No source path found for $fileName!")
