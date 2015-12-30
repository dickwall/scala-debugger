package org.scaladebugger.repl.commands

/**
 * Represents an command that can be performed by the REPL.
 *
 * @tparam T The type of value returned from a successful evaluation
 */
trait Command[T] extends Serializable {
  /**
   * Returns the identifier of the command.
   *
   * @return The unique identifier to lookup the command
   */
  lazy val identifier: String = java.util.UUID.randomUUID().toString

  /**
   * Returns the collection of aliases representing the command.
   *
   * @return The collection of aliases
   */
  def aliases: Seq[String] = Nil

  /**
   * Returns a string of help information about the command.
   *
   * @return The string providing helpful information
   */
  def toHelpString: String

  /**
   * Evaluates the command.
   *
   * @return The result of the evaluation
   */
  def evaluate(): CommandResult[T]
}
