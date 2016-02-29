package org.scaladebugger.api.profiles.pure.info
//import acyclic.file

import com.sun.jdi.{VirtualMachine, StackFrame, Value, ArrayReference}
import org.scaladebugger.api.profiles.traits.info.{ValueInfoProfile, ArrayInfoProfile}

/**
 * Represents a pure implementation of an array profile that adds no custom
 * logic on top of the standard JDI.
 *
 * @param stackFrame The stack frame associated with the array instance
 * @param arrayReference The reference to the underlying JDI array
 * @param virtualMachine The virtual machine used to mirror local values on
 *                       the remote JVM
 */
class PureArrayInfoProfile(
  private val stackFrame: StackFrame,
  private val arrayReference: ArrayReference
)(
  private val virtualMachine: VirtualMachine = stackFrame.virtualMachine()
) extends PureObjectInfoProfile(stackFrame, arrayReference) with ArrayInfoProfile {
  import scala.collection.JavaConverters._
  import org.scaladebugger.api.lowlevel.wrappers.Implicits._

  /**
   * Returns the length of the array.
   *
   * @return The length of the array
   */
  override def length: Int = arrayReference.length()

  /**
   * Retrieves the value in the array at the specified index.
   *
   * @param index The location in the array to retrieve a value
   * @return The retrieved value
   */
  override def getUnsafeValue(index: Int): ValueInfoProfile = {
    newValueProfile(arrayReference.getValue(index))
  }

  /**
   * Sets the values of the array elements starting at the specified location.
   *
   * @param index    The location in the array to begin overwriting
   * @param values   The new values to use when overwriting elements in the array
   * @param srcIndex The location in the provided value array to begin using
   *                 values to overwrite this array
   * @param length   The total number of elements to overwrite, or -1 to overwrite
   *                 all elements in the array from the beginning of the index
   * @return The updated values
   */
  override def setUnsafeValues(
    index: Int,
    values: Seq[Any],
    srcIndex: Int,
    length: Int
  ): Seq[Any] = {
    val v = values.map(virtualMachine.mirrorOf(_: Any)).asJava
    arrayReference.setValues(index, v, srcIndex, length)

    val sliceIndex = if (length >= 0) srcIndex + length else values.length
    values.slice(srcIndex, sliceIndex)
  }

  /**
   * Sets the values of the array elements to the provided values.
   *
   * @param values The new values to use when overwriting elements in the array
   * @return The updated values
   */
  override def setUnsafeValues(values: Seq[Any]): Seq[Any] = {
    val v = values.map(virtualMachine.mirrorOf(_: Any)).asJava
    arrayReference.setValues(v)
    values
  }

  /**
   * Retrieves the values in the array starting from the specified index and
   * continuing through the specified length of elements.
   *
   * @param index  The location in the array to begin retrieving values
   * @param length The number of values to retrieve, or -1 to retrieve all
   *               remaining values to the end of the array
   * @return The retrieved values
   */
  override def getUnsafeValues(
    index: Int,
    length: Int
  ): Seq[ValueInfoProfile] = {
    arrayReference.getValues(index, length).asScala.map(newValueProfile)
  }

  /**
   * Retrieves all values from the array.
   *
   * @return The retrieved values
   */
  override def getUnsafeValues: Seq[ValueInfoProfile] = {
    arrayReference.getValues.asScala.map(newValueProfile)
  }

  /**
   * Sets the value of the array element at the specified location.
   *
   * @param index The location in the array whose value to overwrite
   * @param value The new value to place in the array
   * @return The updated value
   */
  override def setUnsafeValue(index: Int, value: Any): Any = {
    arrayReference.setValue(index, virtualMachine.mirrorOf(value))
    value
  }

  override protected def newValueProfile(value: Value): ValueInfoProfile =
    new PureValueInfoProfile(stackFrame, value)
}
