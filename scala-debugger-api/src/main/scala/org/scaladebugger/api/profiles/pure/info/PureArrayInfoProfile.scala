package org.scaladebugger.api.profiles.pure.info
//import acyclic.file

import com.sun.jdi._
import org.scaladebugger.api.profiles.traits.info._
import org.scaladebugger.api.virtualmachines.ScalaVirtualMachine

import scala.util.Try

/**
 * Represents a pure implementation of an array profile that adds no custom
 * logic on top of the standard JDI.
 *
 * @param scalaVirtualMachine The high-level virtual machine containing the
 *                            array
 * @param infoProducer The producer of info-based profile instances
 * @param _arrayReference The reference to the underlying JDI array
 * @param _virtualMachine The virtual machine used to mirror local values on
 *                       the remote JVM
 * @param _referenceType The reference type for this array
 */
class PureArrayInfoProfile(
  override val scalaVirtualMachine: ScalaVirtualMachine,
  override protected val infoProducer: InfoProducerProfile,
  private val _arrayReference: ArrayReference
)(
  override protected val _virtualMachine: VirtualMachine = _arrayReference.virtualMachine(),
  private val _referenceType: ReferenceType = _arrayReference.referenceType()
) extends PureObjectInfoProfile(scalaVirtualMachine, infoProducer, _arrayReference)(
  _virtualMachine = _virtualMachine,
  _referenceType = _referenceType
) with ArrayInfoProfile with PureCreateInfoProfile {
  import scala.collection.JavaConverters._
  import org.scaladebugger.api.lowlevel.wrappers.Implicits._

  /**
   * Returns the JDI representation this profile instance wraps.
   *
   * @return The JDI instance
   */
  override def toJdiInstance: ArrayReference = _arrayReference

  /**
   * Returns the type information for the array.
   *
   * @return The profile containing type information
   */
  override def typeInfo: ArrayTypeInfoProfile = super.typeInfo.toArrayType

  /**
   * Returns the length of the array.
   *
   * @return The length of the array
   */
  override def length: Int = _arrayReference.length()

  /**
   * Sets the value of the array element at the specified location.
   *
   * @param index The location in the array whose value to overwrite
   * @param value The new value to place in the array
   * @return The updated remote value
   */
  override def setValueFromInfo(
    index: Int,
    value: ValueInfoProfile
  ): ValueInfoProfile = {
    _arrayReference.setValue(index, value.toJdiInstance)
    value
  }

  /**
   * Sets the values of the array elements starting at the specified location.
   *
   * @param index    The location in the array to begin overwriting
   * @param values   The new values to use when overwriting elements in the array
   * @param srcIndex The location in the provided value array to begin using
   *                 values to overwrite this array
   * @param length   The total number of elements to overwrite, or -1 to
   *                 overwrite all elements in the array from the
   *                 beginning of the index
   * @return The updated remote values
   */
  override def setValuesFromInfo(
    index: Int,
    values: Seq[ValueInfoProfile],
    srcIndex: Int,
    length: Int
  ): Seq[ValueInfoProfile] = {
    import scala.collection.JavaConverters._
    val v = values.map(_.toJdiInstance).asJava
    _arrayReference.setValues(index, v, srcIndex, length)

    val sliceIndex = if (length >= 0) srcIndex + length else values.length
    values.slice(srcIndex, sliceIndex)
  }

  /**
   * Sets the values of the array elements to the provided values.
   *
   * @param values The new values to use when overwriting elements in the array
   * @return The updated remote values
   */
  override def setValuesFromInfo(
    values: Seq[ValueInfoProfile]
  ): Seq[ValueInfoProfile] = {
    import scala.collection.JavaConverters._
    _arrayReference.setValues(values.map(_.toJdiInstance).asJava)
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
  override def values(
    index: Int,
    length: Int
  ): Seq[ValueInfoProfile] = {
    _arrayReference.getValues(index, length).asScala.map(newValueProfile)
  }

  /**
   * Retrieves all values from the array.
   *
   * @return The retrieved values
   */
  override def values: Seq[ValueInfoProfile] = {
    _arrayReference.getValues.asScala.map(newValueProfile)
  }

  /**
   * Retrieves the value in the array at the specified index.
   *
   * @param index The location in the array to retrieve a value
   * @return The retrieved value
   */
  override def value(index: Int): ValueInfoProfile = {
    newValueProfile(_arrayReference.getValue(index))
  }

  override protected def newValueProfile(value: Value): ValueInfoProfile =
    infoProducer.newValueInfoProfile(scalaVirtualMachine, value)
}
