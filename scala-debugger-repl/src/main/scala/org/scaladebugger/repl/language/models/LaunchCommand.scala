package org.scaladebugger.repl.language.models

/**
 * Represents a command to launch a JVM.
 *
 * @param className The fully qualified class name to use as the entrypoint
 *                  when launching the JVM
 * @param commandLineArguments Any command line arguments to provide to the
 *                             new JVM's main method
 * @param jvmOptions Any JVM options to provide to the new JVM
 * @param suspend Whether or not to suspend the JVM until the debugger connects
 */
case class LaunchCommand(
  className: String,
  commandLineArguments: Seq[String],
  jvmOptions: Seq[String],
  suspend: Boolean
) extends Command
