package org.scaladebugger.api.profiles.traits.info
//import acyclic.file

import scala.util.Try

/**
 * Represents the interface for method-based interaction.
 */
trait MethodInfoProfile {
  /**
   * Returns the name of this method.
   *
   * @return The name of the method
   */
  def name: String

  /**
   * Returns the fully-qualified class names of the types for the parameters
   * of this method.
   *
   * @return Success containing the collection of parameter type names,
   *         otherwise a failure
   */
  def parameterTypeNames: Try[Seq[String]] = Try(unsafeParameterTypeNames)

  /**
   * Returns the fully-qualified class names of the types for the parameters
   * of this method.
   *
   * @return The collection of parameter type names
   */
  def unsafeParameterTypeNames: Seq[String]

  /**
   * Returns the fully-qualified class name of the type for the return value
   * of this method.
   *
   * @return Success containing the return type name, otherwise a failure
   */
  def returnTypeName: Try[String] = Try(unsafeReturnTypeName)

  /**
   * Returns the fully-qualified class name of the type for the return value
   * of this method.
   *
   * @return The return type name
   */
  def unsafeReturnTypeName: String
}
