package org.scaladebugger.repl.language.models

/**
 * Represents a command to attach to a JVM.
 *
 * @param port The port of the remote JVM to connect with when attaching
 * @param hostname The hostname of the remote JVM to connect with when attaching
 * @param timeout The maximum time to wait (in milliseconds) to connect to
 *                the remote JVM
 */
case class AttachCommand(
  port: Int,
  hostname: String,
  timeout: Long
) extends Command
