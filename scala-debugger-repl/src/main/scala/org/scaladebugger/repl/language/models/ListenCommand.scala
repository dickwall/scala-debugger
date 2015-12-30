package org.scaladebugger.repl.language.models

/**
 * Represents a command to listen for JVMs.
 *
 * @param port The port of the remote JVM to connect with when attaching
 * @param hostname The hostname of the remote JVM to connect with when attaching
 * @param workers The maximum number of workers to process new connections
 */
case class ListenCommand(
  port: Int,
  hostname: String,
  workers: Int
) extends Command
