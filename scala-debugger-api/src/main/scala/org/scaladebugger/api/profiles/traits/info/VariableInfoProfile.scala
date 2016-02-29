package org.scaladebugger.api.profiles.traits.info
//import acyclic.file

import scala.util.Try

/**
 * Represents the interface for variable-based interaction.
 */
trait VariableInfoProfile {
  /**
   * Returns the name of the variable.
   *
   * @return The name of the variable
   */
  def name: String

  /**
   * Returns whether or not this variable represents a field.
   *
   * @return True if a field, otherwise false
   */
  def isField: Boolean

  /**
   * Returns whether or not this variable represents a local variable.
   *
   * @return True if a local variable, otherwise false
   */
  def isLocal: Boolean

  /**
   * Returns whether or not this variable represents an argument.
   *
   * @return True if an argument, otherwise false
   */
  def isArgument: Boolean

  /**
   * Returns a profile representing the value of this variable.
   *
   * @return Success containing the profile representing the value, otherwise
   *         a failure
   */
  def toValue: Try[ValueInfoProfile] = Try(toUnsafeValue)

  /**
   * Returns a profile representing the value of this variable.
   *
   * @return The profile representing the value
   */
  def toUnsafeValue: ValueInfoProfile

  /**
   * Sets the primitive value of this variable.
   *
   * @param value The new value for the variable
   *
   * @return Success containing the value, otherwise a failure
   */
  def setValue(value: AnyVal): Try[AnyVal]

  /**
   * Sets the string value of this variable.
   *
   * @param value The new value for the variable
   *
   * @return Success containing the value, otherwise a failure
   */
  def setValue(value: String): Try[String]
}
