package org.scaladebugger.api.profiles.traits.info
//import acyclic.file

import scala.util.Try

/**
 * Represents the interface for frame-based interaction.
 */
trait FrameInfoProfile {
  /**
   * Retrieves the object representing 'this' in the current frame scope.
   *
   * @return Success containing the profile of this object, otherwise a failure
   */
  def withThisObject: Try[ObjectInfoProfile] = Try(withUnsafeThisObject)

  /**
   * Retrieves the object representing 'this' in the current frame scope.
   *
   * @return The profile of this object
   */
  def withUnsafeThisObject: ObjectInfoProfile

  /**
   * Retrieves the thread associated with this frame.
   *
   * @return Success containing the profile of the thread, otherwise a failure
   */
  def withCurrentThread: Try[ThreadInfoProfile] = Try(withUnsafeCurrentThread)

  /**
   * Retrieves the thread associated with this frame.
   *
   * @return The profile of the thread
   */
  def withUnsafeCurrentThread: ThreadInfoProfile

  /**
   * Retrieves the variable with the specified name from the frame.
   *
   * @param name The name of the variable to retrieve
   *
   * @return Success containing profile of the variable if found, otherwise
   *         a failure
   */
  def forVariable(name: String): Try[VariableInfoProfile] =
    Try(forUnsafeVariable(name))

  /**
   * Retrieves the variable with the specified name from the frame.
   *
   * @param name The name of the variable to retrieve
   *
   * @return Profile of the variable or throws an exception
   */
  def forUnsafeVariable(name: String): VariableInfoProfile

  /**
   * Retrieves all variables in this frame.
   *
   * @return Success containing the collection of variables as their profile
   *         equivalents, otherwise a failure
   */
  def forAllVariables: Try[Seq[VariableInfoProfile]] =
    Try(forUnsafeAllVariables)

  /**
   * Retrieves all variables that represent arguments in this frame.
   *
   * @return Success containing the collection of variables as their profile
   *         equivalents, otherwise a failure
   */
  def forArguments: Try[Seq[VariableInfoProfile]] = Try(forUnsafeArguments)

  /**
   * Retrieves all variables that do not represent arguments in this frame.
   *
   * @return Success containing the collection of variables as their profile
   *         equivalents, otherwise a failure
   */
  def forNonArguments: Try[Seq[VariableInfoProfile]] =
    Try(forUnsafeNonArguments)

  /**
   * Retrieves all variables that represent local variables in this frame.
   *
   * @return Success containing the collection of variables as their profile
   *         equivalents, otherwise a failure
   */
  def forLocalVariables: Try[Seq[VariableInfoProfile]] =
    Try(forUnsafeLocalVariables)

  /**
   * Retrieves all variables that represent field variables in this frame.
   *
   * @return Success containing the collection of variables as their profile
   *         equivalents, otherwise a failure
   */
  def forFieldVariables: Try[Seq[VariableInfoProfile]] =
    Try(forUnsafeFieldVariables)

  /**
   * Retrieves all variables in this frame.
   *
   * @return The collection of variables as their profile equivalents
   */
  def forUnsafeAllVariables: Seq[VariableInfoProfile]

  /**
   * Retrieves all variables that represent arguments in this frame.
   *
   * @return The collection of variables as their profile equivalents
   */
  def forUnsafeArguments: Seq[VariableInfoProfile]

  /**
   * Retrieves all variables that do not represent arguments in this frame.
   *
   * @return The collection of variables as their profile equivalents
   */
  def forUnsafeNonArguments: Seq[VariableInfoProfile]

  /**
   * Retrieves all variables that represent local variables in this frame.
   *
   * @return The collection of variables as their profile equivalents
   */
  def forUnsafeLocalVariables: Seq[VariableInfoProfile]

  /**
   * Retrieves all variables that represent field variables in this frame.
   *
   * @return The collection of variables as their profile equivalents
   */
  def forUnsafeFieldVariables: Seq[VariableInfoProfile]
}
