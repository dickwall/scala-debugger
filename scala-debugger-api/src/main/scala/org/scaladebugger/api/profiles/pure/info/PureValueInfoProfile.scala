package org.scaladebugger.api.profiles.pure.info
//import acyclic.file

import com.sun.jdi._
import org.scaladebugger.api.profiles.traits.info.{ArrayInfoProfile, ObjectInfoProfile, ValueInfoProfile}

import scala.util.Try

object PureValueInfoProfile {
  val DefaultNullTypeName = "null"
}

/**
 * Represents a pure implementation of a value profile that adds no custom
 * logic on top of the standard JDI.
 *
 * @param stackFrame The stack frame associated with the value instance
 * @param value The reference to the underlying JDI value
 */
class PureValueInfoProfile(
  private val stackFrame: StackFrame,
  private val value: Value
) extends ValueInfoProfile {
  /**
   * Returns the type name of this value.
   *
   * @return The type name (typically a fully-qualified class name)
   */
  override def typeName: String =
    if (!isNull) value.`type`().name()
    else PureValueInfoProfile.DefaultNullTypeName

  /**
   * Returns the value as an array (profile).
   *
   * @return The array profile wrapping this value
   */
  @throws[AssertionError]
  override def asUnsafeArray: ArrayInfoProfile = {
    assert(isArray, "Value must be an array!")
    newArrayProfile(value.asInstanceOf[ArrayReference])
  }

  /**
   * Returns the value as a value local to this JVM.
   *
   * @return The value as a local instance
   */
  override def asUnsafeLocalValue: Any = {
    import org.scaladebugger.api.lowlevel.wrappers.Implicits._
    if (!isNull) value.value()
    else null
  }

  /**
   * Returns the value as an object (profile).
   *
   * @return The object profile wrapping this value
   */
  @throws[AssertionError]
  override def asUnsafeObject: ObjectInfoProfile = {
    assert(isObject, "Value must be an object!")
    newObjectProfile(value.asInstanceOf[ObjectReference])
  }

  /**
   * Returns whether or not this value represents a primitive.
   *
   * @return True if a primitive, otherwise false
   */
  override def isPrimitive: Boolean =
    !isNull && value.isInstanceOf[PrimitiveValue]

  /**
   * Returns whether or not this value represents an array.
   *
   * @return True if an array, otherwise false
   */
  override def isArray: Boolean =
    !isNull && value.isInstanceOf[ArrayReference]

  /**
   * Returns whether or not this value represents an object.
   *
   * @return True if an object, otherwise false
   */
  override def isObject: Boolean =
    !isNull && value.isInstanceOf[ObjectReference]

  /**
   * Returns whether or not this value represents a string.
   *
   * @return True if a string, otherwise false
   */
  override def isString: Boolean =
    !isNull && value.isInstanceOf[StringReference]

  /**
   * Returns whether or not this value is null.
   *
   * @return True if null, otherwise false
   */
  override def isNull: Boolean = value == null

  protected def newObjectProfile(objectReference: ObjectReference): ObjectInfoProfile =
    new PureObjectInfoProfile(stackFrame, objectReference)

  protected def newArrayProfile(arrayReference: ArrayReference): ArrayInfoProfile =
    new PureArrayInfoProfile(stackFrame, arrayReference)()
}
