package org.scaladebugger.api.profiles.traits.info
//import acyclic.file

import org.scaladebugger.api.lowlevel.JDIArgument

import scala.util.Try

/**
 * Represents the interface for object-based interaction.
 */
trait ObjectInfoProfile extends ValueInfoProfile {
  /**
   * Invokes the object's method with matching name and arguments.
   *
   * @param methodName The name of the method to invoke
   * @param arguments The arguments to provide to the method
   * @param jdiArguments Optional arguments to provide custom settings to the
   *                     method invocation
   * @return Success containing the resulting value of the invocation, otherwise
   *         a failure
   */
  def invoke(
    methodName: String,
    arguments: Seq[Any],
    jdiArguments: JDIArgument*
  ): Try[ValueInfoProfile] = invoke(
    methodName,
    arguments.map(_.getClass.getName),
    arguments,
    jdiArguments: _*
  )

  /**
   * Invokes the object's method with matching name and arguments.
   *
   * @param methodName The name of the method to invoke
   * @param arguments The arguments to provide to the method
   * @param jdiArguments Optional arguments to provide custom settings to the
   *                     method invocation
   * @return The resulting value of the invocation
   */
  def unsafeInvoke(
    methodName: String,
    arguments: Seq[Any],
    jdiArguments: JDIArgument*
  ): ValueInfoProfile = unsafeInvoke(
    methodName,
    arguments.map(_.getClass.getName),
    arguments,
    jdiArguments: _*
  )

  /**
   * Invokes the object's method with matching name and arguments.
   *
   * @param methodName The name of the method to invoke
   * @param parameterTypeNames The names of the parameter types of the method
   *                           to invoke
   * @param arguments The arguments to provide to the method
   * @param jdiArguments Optional arguments to provide custom settings to the
   *                     method invocation
   * @return Success containing the resulting value of the invocation, otherwise
   *         a failure
   */
  def invoke(
    methodName: String,
    parameterTypeNames: Seq[String],
    arguments: Seq[Any],
    jdiArguments: JDIArgument*
  ): Try[ValueInfoProfile] = Try(unsafeInvoke(
    methodName,
    parameterTypeNames,
    arguments,
    jdiArguments: _*
  ))

  /**
   * Invokes the object's method with matching name and arguments.
   *
   * @param methodName The name of the method to invoke
   * @param parameterTypeNames The names of the parameter types of the method
   *                           to invoke
   * @param arguments The arguments to provide to the method
   * @param jdiArguments Optional arguments to provide custom settings to the
   *                     method invocation
   * @return The resulting value of the invocation
   */
  def unsafeInvoke(
    methodName: String,
    parameterTypeNames: Seq[String],
    arguments: Seq[Any],
    jdiArguments: JDIArgument*
  ): ValueInfoProfile

  /**
   * Invokes the object's method.
   *
   * @param methodInfoProfile The method of the object to invoke
   * @param arguments The arguments to provide to the method
   * @param jdiArguments Optional arguments to provide custom settings to the
   *                     method invocation
   * @return Success containing the resulting value of the invocation, otherwise
   *         a failure
   */
  def invoke(
    methodInfoProfile: MethodInfoProfile,
    arguments: Seq[Any],
    jdiArguments: JDIArgument*
  ): Try[ValueInfoProfile] = Try(unsafeInvoke(
    methodInfoProfile,
    arguments,
    jdiArguments: _*
  ))

  /**
   * Invokes the object's method.
   *
   * @param methodInfoProfile The method of the object to invoke
   * @param arguments The arguments to provide to the method
   * @param jdiArguments Optional arguments to provide custom settings to the
   *                     method invocation
   * @return The resulting value of the invocation
   */
  def unsafeInvoke(
    methodInfoProfile: MethodInfoProfile,
    arguments: Seq[Any],
    jdiArguments: JDIArgument*
  ): ValueInfoProfile

  /**
   * Returns all visible fields contained in this object.
   *
   * @return Success containing the profiles wrapping the visible fields in
   *         this object, otherwise a failure
   */
  def fields: Try[Seq[VariableInfoProfile]] = Try(unsafeFields)

  /**
   * Returns all visible fields contained in this object.
   *
   * @return The profiles wrapping the visible fields in this object
   */
  def unsafeFields: Seq[VariableInfoProfile]

  /**
   * Returns the object's field with the specified name.
   *
   * @param name The name of the field
   * @return Success containing the profile wrapping the field, otherwise
   *         a failure
   */
  def field(name: String): Try[VariableInfoProfile] = Try(unsafeField(name))

  /**
   * Returns the object's field with the specified name.
   *
   * @param name The name of the field
   * @return The profile wrapping the field
   */
  def unsafeField(name: String): VariableInfoProfile

  /**
   * Returns all visible methods contained in this object.
   *
   * @return Success containing the profiles wrapping the visible methods in
   *         this object, otherwise a failure
   */
  def methods: Try[Seq[MethodInfoProfile]] = Try(unsafeMethods)

  /**
   * Returns all visible methods contained in this object.
   *
   * @return The profiles wrapping the visible methods in this object
   */
  def unsafeMethods: Seq[MethodInfoProfile]

  /**
   * Returns the object's method with the specified name.
   *
   * @param name The name of the method
   * @param parameterTypeNames The fully-qualified type names of the parameters
   *                           of the method to find
   * @return Success containing the profile wrapping the method, otherwise
   *         a failure
   */
  def method(
    name: String,
    parameterTypeNames: String*
  ): Try[MethodInfoProfile] = Try(unsafeMethod(name, parameterTypeNames: _*))

  /**
   * Returns the object's method with the specified name.
   *
   * @param name The name of the method
   * @param parameterTypeNames The fully-qualified type names of the parameters
   *                           of the method to find
   * @return The profile wrapping the method
   */
  def unsafeMethod(
    name: String,
    parameterTypeNames: String*
  ): MethodInfoProfile
}
