package org.scaladebugger.repl.commands

sealed abstract class CommandResult[+T] {
  def get: T
  def msg: String
}
case class Success[+T](result: T, override val msg: String) extends CommandResult[T] {
  def get: T = result
}

sealed abstract class NoSuccess(override val msg: String) extends CommandResult[Nothing] {
  def get: Nothing = sys.error("No result when command failed")
}

/** Normal, recoverable error */
case class Failure(override val msg: String) extends NoSuccess(msg)

/** Fatal error */
case class Error(override val msg: String) extends NoSuccess(msg)
