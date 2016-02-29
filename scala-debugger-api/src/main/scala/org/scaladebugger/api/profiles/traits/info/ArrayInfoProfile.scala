package org.scaladebugger.api.profiles.traits.info
//import acyclic.file

import scala.util.Try

/**
 * Represents the interface for array-based interaction.
 */
trait ArrayInfoProfile extends ObjectInfoProfile {
  /**
   * Returns the length of the array.
   *
   * @return The length of the array
   */
  def length: Int

  /**
   * Retrieves the value in the array at the specified index.
   *
   * @param index The location in the array to retrieve a value
   *
   * @return Success containing the retrieved value, otherwise a failure
   */
  def getValue(index: Int): Try[ValueInfoProfile] = Try(getUnsafeValue(index))

  /**
   * Retrieves the value in the array at the specified index.
   *
   * @param index The location in the array to retrieve a value
   *
   * @return The retrieved value
   */
  def getUnsafeValue(index: Int): ValueInfoProfile

  /**
   * Retrieves the value in the array at the specified index.
   *
   * @param index The location in the array to retrieve a value
   *
   * @return The retrieved value
   */
  def apply(index: Int): ValueInfoProfile = getUnsafeValue(index)

  /**
   * Retrieves the values in the array starting from the specified index and
   * continuing through the specified length of elements.
   *
   * @param index The location in the array to begin retrieving values
   * @param length The number of values to retrieve, or -1 to retrieve all
   *               remaining values to the end of the array
   *
   * @return Success containing the retrieved values, otherwise a failure
   */
  def getValues(index: Int, length: Int): Try[Seq[ValueInfoProfile]] =
    Try(getUnsafeValues(index, length))

  /**
   * Retrieves the values in the array starting from the specified index and
   * continuing through the specified length of elements.
   *
   * @param index The location in the array to begin retrieving values
   * @param length The number of values to retrieve, or -1 to retrieve
   *               all remaining values to the end of the array
   *
   * @return The retrieved values
   */
  def getUnsafeValues(index: Int, length: Int): Seq[ValueInfoProfile]

  /**
   * Retrieves the values in the array starting from the specified index and
   * continuing through the specified length of elements.
   *
   * @param index The location in the array to begin retrieving values
   * @param length The number of values to retrieve, or -1 to retrieve
   *               all remaining values to the end of the array
   * @return The retrieved values
   */
  def apply(index: Int, length: Int): Seq[ValueInfoProfile] =
    getUnsafeValues(index, length)

  /**
   * Retrieves all values from the array.
   *
   * @return Success containing the retrieved values, otherwise a failure
   */
  def getValues: Try[Seq[ValueInfoProfile]] = Try(getUnsafeValues)

  /**
   * Retrieves all values from the array.
   *
   * @return The retrieved values
   */
  def getUnsafeValues: Seq[ValueInfoProfile]

  /**
   * Retrieves all values from the array.
   *
   * @return The retrieved values
   */
  def apply(): Seq[ValueInfoProfile] = getUnsafeValues

  /**
   * Sets the value of the array element at the specified location.
   *
   * @param index The location in the array whose value to overwrite
   * @param value The new value to place in the array
   *
   * @return Success containing the updated value, otherwise a failure
   */
  def setValue(index: Int, value: Any): Try[Any] =
    Try(setUnsafeValue(index, value))

  /**
   * Sets the value of the array element at the specified location.
   *
   * @param index The location in the array whose value to overwrite
   * @param value The new value to place in the array
   *
   * @return The updated value
   */
  def setUnsafeValue(index: Int, value: Any): Any

  /**
   * Sets the value of the array element at the specified location.
   *
   * @param index The location in the array whose value to overwrite
   * @param value The new value to place in the array
   *
   * @return The updated value
   */
  def update(index: Int, value: Any): Any = setUnsafeValue(index, value)

  /**
   * Sets the values of the array elements starting at the specified location.
   *
   * @param index The location in the array to begin overwriting
   * @param values The new values to use when overwriting elements in the array
   * @param srcIndex The location in the provided value array to begin using
   *                 values to overwrite this array
   * @param length The total number of elements to overwrite, or -1 to
   *               overwrite all elements in the array from the
   *               beginning of the index
   * @return Success containing the updated values, otherwise a failure
   */
  def setValues(
    index: Int,
    values: Seq[Any],
    srcIndex: Int,
    length: Int
  ): Try[Seq[Any]] = Try(setUnsafeValues(index, values, srcIndex, length))

  /**
   * Sets the values of the array elements starting at the specified location.
   *
   * @param index The location in the array to begin overwriting
   * @param values The new values to use when overwriting elements in the array
   * @param srcIndex The location in the provided value array to begin using
   *                 values to overwrite this array
   * @param length The total number of elements to overwrite, or -1 to
   *               overwrite all elements in the array from the
   *               beginning of the index
   *
   * @return The updated values
   */
  def setUnsafeValues(
    index: Int,
    values: Seq[Any],
    srcIndex: Int,
    length: Int
  ): Seq[Any]

  /**
   * Sets the values of the array elements to the provided values.
   *
   * @param values The new values to use when overwriting elements in the array
   *
   * @return Success containing the updated values, otherwise a failure
   */
  def setValues(values: Seq[Any]): Try[Seq[Any]] =
    Try(setUnsafeValues(values))

  /**
   * Sets the values of the array elements to the provided values.
   *
   * @param values The new values to use when overwriting elements in the array
   *
   * @return The updated values
   */
  def setUnsafeValues(values: Seq[Any]): Seq[Any]
}
