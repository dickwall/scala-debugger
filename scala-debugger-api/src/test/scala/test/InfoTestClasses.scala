package test

import com.sun.jdi.ThreadReference
import org.scaladebugger.api.lowlevel.JDIArgument
import org.scaladebugger.api.profiles.traits.info._

import scala.util.Try

/**
 * Contains test implementations of info classes. All methods throw an error.
 */
object InfoTestClasses {
  class NotOverriddenException extends Exception

  /** Exception thrown by all methods. */
  val DefaultException = new NotOverriddenException
  private def throwException() = throw DefaultException

  class TestThreadInfoProfile extends ThreadInfoProfile {
    override def uniqueId: Long = throwException()
    override def unsafeFrames: Seq[FrameInfoProfile] = throwException()
    override def name: String = throwException()
    override def withUnsafeFrame(index: Int): FrameInfoProfile = throwException()
    override def unsafeTotalFrames: Int = throwException()
  }

  class TestValueInfoProfile extends ValueInfoProfile {
    override def typeName: String = throwException()
    override def isObject: Boolean = throwException()
    override def isPrimitive: Boolean = throwException()
    override def asUnsafeObject: ObjectInfoProfile = throwException()
    override def asUnsafeLocalValue: Any = throwException()
    override def asUnsafeArray: ArrayInfoProfile = throwException()
    override def isString: Boolean = throwException()
    override def isArray: Boolean = throwException()
    override def isNull: Boolean = throwException()
  }

  class TestVariableInfoProfile extends VariableInfoProfile {
    override def name: String = throwException()
    override def toUnsafeValue: ValueInfoProfile = throwException()
    override def setValue(value: AnyVal): Try[AnyVal] = throwException()
    override def setValue(value: String): Try[String] = throwException()
    override def isArgument: Boolean = throwException()
    override def isLocal: Boolean = throwException()
    override def isField: Boolean = throwException()
  }

  class TestObjectInfoProfile extends TestValueInfoProfile with ObjectInfoProfile {
    override def unsafeInvoke(methodInfoProfile: MethodInfoProfile, arguments: Seq[Any], jdiArguments: JDIArgument*): ValueInfoProfile = throwException()
    override def unsafeInvoke(methodName: String, parameterTypeNames: Seq[String], arguments: Seq[Any], jdiArguments: JDIArgument*): ValueInfoProfile = throwException()
    override def unsafeMethod(name: String, parameterTypeNames: String*): MethodInfoProfile = throwException()
    override def unsafeFields: Seq[VariableInfoProfile] = throwException()
    override def unsafeField(name: String): VariableInfoProfile = throwException()
    override def unsafeMethods: Seq[MethodInfoProfile] = throwException()
  }

  class TestMethodInfoProfile extends MethodInfoProfile {
    override def name: String = throwException()
    override def unsafeReturnTypeName: String = throwException()
    override def unsafeParameterTypeNames: Seq[String] = throwException()
  }

  class TestGrabInfoProfile extends GrabInfoProfile {
    override def forUnsafeThread(threadReference: ThreadReference): ThreadInfoProfile = throwException()
    override def forUnsafeThread(threadId: Long): ThreadInfoProfile = throwException()
  }

  class TestFrameInfoProfile extends FrameInfoProfile {
    override def withUnsafeThisObject: ObjectInfoProfile = throwException()
    override def withUnsafeCurrentThread: ThreadInfoProfile = throwException()
    override def forUnsafeFieldVariables: Seq[VariableInfoProfile] = throwException()
    override def forUnsafeVariable(name: String): VariableInfoProfile = throwException()
    override def forUnsafeAllVariables: Seq[VariableInfoProfile] = throwException()
    override def forUnsafeLocalVariables: Seq[VariableInfoProfile] = throwException()
    override def forUnsafeNonArguments: Seq[VariableInfoProfile] = throwException()
    override def forUnsafeArguments: Seq[VariableInfoProfile] = throwException()
  }

  class TestArrayInfoProfile extends TestObjectInfoProfile with ArrayInfoProfile {
    override def length: Int = throwException()
    override def getUnsafeValue(index: Int): ValueInfoProfile = throwException()
    override def setUnsafeValues(index: Int, values: Seq[Any], srcIndex: Int, length: Int): Seq[Any] = throwException()
    override def setUnsafeValues(values: Seq[Any]): Seq[Any] = throwException()
    override def getUnsafeValues(index: Int, length: Int): Seq[ValueInfoProfile] = throwException()
    override def getUnsafeValues: Seq[ValueInfoProfile] = throwException()
    override def setUnsafeValue(index: Int, value: Any): Any = throwException()
  }
}
