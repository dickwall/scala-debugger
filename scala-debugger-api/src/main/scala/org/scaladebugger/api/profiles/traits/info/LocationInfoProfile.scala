package org.scaladebugger.api.profiles.traits.info

import com.sun.jdi.Location

import scala.util.Try

/**
 * Represents the interface for location-based interaction.
 */
trait LocationInfoProfile extends CommonInfoProfile {
  /**
   * Returns the JDI representation this profile instance wraps.
   *
   * @return The JDI instance
   */
  override def toJdiInstance: Location

  /**
   * Returns the type to which this location belongs.
   *
   * @return The reference type profile instance
   */
  def getDeclaringType: ReferenceTypeInfoProfile

  /**
   * Returns the type to which this location belongs.
   *
   * @return Success containing the reference type profile instance, otherwise
   *         a failure
   */
  def tryGetDeclaringType: Try[ReferenceTypeInfoProfile] = Try(getDeclaringType)

  /**
   * Returns the method associated with this location.
   *
   * @return The method profile instance
   */
  def getMethod: MethodInfoProfile

  /**
   * Returns the method associated with this location.
   *
   * @return Success containing the method profile instance, otherwise a failure
   */
  def tryGetMethod: Try[MethodInfoProfile] = Try(getMethod)

  /**
   * Returns the code position relative to the location's method.
   *
   * @return The code position relative to the location's method, or -1 if not
   *         available (such as in a native method)
   */
  def getCodeIndex: Long

  /**
   * Returns the code position relative to the location's method.
   *
   * @return Some code position relative to the location's method, or None if
   *         not available (such as in a native method)
   */
  def getCodeIndexOption: Option[Long] = Option(getCodeIndex).filter(_ >= 0)

  /**
   * Returns the line number corresponding to this location relative to the
   * associated file's source code.
   *
   * @return The line number corresponding to the location, or -1 if not
   *         available (such as in a native method)
   */
  def getLineNumber: Int

  /**
   * Returns the line number corresponding to this location relative to the
   * associated file's source code.
   *
   * @return Some line number corresponding to the location, or None if not
   *         available (such as in a native method)
   */
  def getLineNumberOption: Option[Int] = Option(getLineNumber).filter(_ >= 0)

  /**
   * Retrieves the source name of the location.
   *
   * @example getSourceName returns Debugger.scala
   *
   * @return The name of the source file containing this location
   */
  def getSourceName: String

  /**
   * Retrieves the source name of the location.
   *
   * @example tryGetSourceName returns Success(Debugger.scala)
   *
   * @return Success containing the name of the source file containing this
   *         location, otherwise a failure
   */
  def tryGetSourceName: Try[String] = Try(getSourceName)

  /**
   * Retrieves the source path of the location.
   *
   * @example getSourcePath returns
   *          org/scaladebugger/api/debuggers/Debugger.scala
   *
   * @return The full path to the source file containing this location
   */
  def getSourcePath: String

  /**
   * Retrieves the source path of the location.
   *
   * @example tryGetSourcePath returns
   *          Success(org/scaladebugger/api/debuggers/Debugger.scala)
   *
   * @return Success containing the full path to the source file containing
   *         this location, otherwise a failure
   */
  def tryGetSourcePath: Try[String] = Try(getSourcePath)
}
