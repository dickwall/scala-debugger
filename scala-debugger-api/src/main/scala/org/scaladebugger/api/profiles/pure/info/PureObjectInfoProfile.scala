package org.scaladebugger.api.profiles.pure.info
//import acyclic.file

import com.sun.jdi._
import org.scaladebugger.api.lowlevel.{InvokeNonVirtualArgument, InvokeSingleThreadedArgument, JDIArgument}
import org.scaladebugger.api.profiles.traits.info._

import scala.util.Try
import scala.collection.JavaConverters._

/**
 * Represents a pure implementation of an object profile that adds no custom
 * logic on top of the standard JDI.
 *
 * @param stackFrame The stack frame associated with the object instance
 * @param objectReference The reference to the underlying JDI object
 */
class PureObjectInfoProfile(
  private val stackFrame: StackFrame,
  private val objectReference: ObjectReference
) extends PureValueInfoProfile(stackFrame, objectReference) with ObjectInfoProfile {
  private lazy val referenceType = objectReference.referenceType()
  private lazy val typeChecker = newTypeCheckerProfile()

  /**
   * Invokes the object's method.
   *
   * @param methodInfoProfile The method of the object to invoke
   * @param arguments         The arguments to provide to the method
   * @param jdiArguments      Optional arguments to provide custom settings to the
   *                          method invocation
   * @return The resulting value of the invocation
   */
  override def unsafeInvoke(
    methodInfoProfile: MethodInfoProfile,
    arguments: Seq[Any],
    jdiArguments: JDIArgument*
  ): ValueInfoProfile = {
    val m = methodInfoProfile match {
      case p: PureMethodInfoProfile => p.method
    }

    import org.scaladebugger.api.lowlevel.wrappers.Implicits._
    lazy val virtualMachine = stackFrame.virtualMachine()
    val v = arguments.map(virtualMachine.mirrorOf(_: Any))

    val o = jdiArguments.map {
      case InvokeSingleThreadedArgument => ObjectReference.INVOKE_SINGLE_THREADED
      case InvokeNonVirtualArgument     => ObjectReference.INVOKE_NONVIRTUAL
    }.fold(0)(_ | _)

    val r = objectReference.invokeMethod(stackFrame.thread(), m, v.asJava, o)
    newValueProfile(r)
  }

  /**
   * Invokes the object's method with matching name and arguments.
   *
   * @param methodName         The name of the method to invoke
   * @param parameterTypeNames The names of the parameter types of the method
   *                           to invoke
   * @param arguments          The arguments to provide to the method
   * @param jdiArguments       Optional arguments to provide custom settings to the
   *                           method invocation
   * @return The resulting value of the invocation
   */
  override def unsafeInvoke(
    methodName: String,
    parameterTypeNames: Seq[String],
    arguments: Seq[Any],
    jdiArguments: JDIArgument*
  ): ValueInfoProfile = {
    assert(parameterTypeNames.length == arguments.length,
      "Inconsistent number of parameter types versus arguments!")

    unsafeInvoke(
      unsafeMethod(methodName, parameterTypeNames: _*),
      arguments,
      jdiArguments: _*
    )
  }

  /**
   * Returns all visible methods contained in this object.
   *
   * @return The profiles wrapping the visible methods in this object
   */
  override def unsafeMethods: Seq[MethodInfoProfile] = {
    referenceType.visibleMethods().asScala.map(newMethodProfile)
  }

  /**
   * Returns the object's method with the specified name.
   *
   * @param name               The name of the method
   * @param parameterTypeNames The fully-qualified type names of the parameters
   *                           of the method to find
   * @return The profile wrapping the method
   */
  override def unsafeMethod(
    name: String,
    parameterTypeNames: String*
  ): MethodInfoProfile = {
    referenceType.methodsByName(name).asScala.find(
      _.argumentTypeNames().asScala.zip(parameterTypeNames)
        .forall(s => typeChecker.equalTypeNames(s._1, s._2))
    ).map(newMethodProfile).get
  }

  /**
   * Returns all visible fields contained in this object.
   *
   * @return The profiles wrapping the visible fields in this object
   */
  override def unsafeFields: Seq[VariableInfoProfile] = {
    referenceType.visibleFields().asScala.map(newFieldProfile)
  }

  /**
   * Returns the object's field with the specified name.
   *
   * @param name The name of the field
   * @return The profile wrapping the field
   */
  override def unsafeField(name: String): VariableInfoProfile = {
    newFieldProfile(Option(referenceType.fieldByName(name)).get)
  }

  protected def newFieldProfile(field: Field): VariableInfoProfile =
    new PureFieldInfoProfile(stackFrame, field)()

  protected def newMethodProfile(method: Method): MethodInfoProfile =
    new PureMethodInfoProfile(method)

  protected def newValueProfile(value: Value): ValueInfoProfile =
    new PureValueInfoProfile(stackFrame, value)

  protected def newTypeCheckerProfile(): TypeCheckerProfile =
    new PureTypeCheckerProfile
}
