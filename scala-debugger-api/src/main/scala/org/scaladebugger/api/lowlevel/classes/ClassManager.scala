package org.scaladebugger.api.lowlevel.classes
import acyclic.file

import com.sun.jdi.{Location, ReferenceType}


/**
 * Represents a manager of classes available on the virtual machine and their
 * associated files.
 */
trait ClassManager {
  /**
   * Retrieves the mapping of lines to locations available for a specific file.
   *
   * @param fileName The name of the file whose lines and underlying
   *                  locations to retrieve
   *
   * @return Some mapping of file lines to associated locations in underlying
   *         JVM classes if the file exists, otherwise None
   */
  def linesAndLocationsForFile(
    fileName: String
  ): Option[Map[Int, Seq[Location]]]

  /**
   * Retrieves the list of underlying JVM classes for the specified file.
   *
   * @param fileName The name of the file whose underlying representations
   *                  to retrieve
   *
   * @return Some list of underlying class references if the file name can
   *         be found, otherwise None
   */
  def underlyingReferencesForFile(fileName: String): Option[Seq[ReferenceType]]

  /**
   * Refresh the list of classes contained by the underlying virtual machine.
   * Groups by source path, falling back to a standard "ARRAY" grouping for
   * references to array structures and "UNKNOWN" for references with no
   * source name or known name.
   */
  def refreshAllClasses(): Unit

  /**
   * Refresh a single class given the reference type.
   *
   * @param referenceType The reference type used for the refresh
   */
  def refreshClass(referenceType: ReferenceType): Unit

  /**
   * Retrieves the file name for the associated reference type.
   *
   * @param referenceType The reference type whose file name to retrieve
   *
   * @return The file name if it exists, otherwise ARRAY if the reference type
   *         is an array or UNKNOWN if it is not
   */
  def fileNameForReferenceType(referenceType: ReferenceType): String

  /**
   * Retrieves a list of available (cached) Scala file names.
   *
   * @return The collection of file names
   */
  def allScalaFileNames: Seq[String]

  /**
   * Retrieves a list of available (cached) Java file names.
   *
   * @return The collection of file names
   */
  def allJavaFileNames: Seq[String]

  /**
   * Retrieves a list of available (cached) file names with the provided
   * extension.
   *
   * @param extension The extension of the file names (Scala/Java/etc)
   *
   * @return The collection of file names
   */
  def allFileNamesWithExtension(extension: String): Seq[String]

  /**
   * Retrieves a list of all available (cached) file names.
   *
   * @return The collection of file names
   */
  def allFileNames: Seq[String]

  /**
   * Retrieves a list of all available (cached) classes.
   *
   * @return The collection of reference types
   */
  def allClasses: Seq[ReferenceType]
}
