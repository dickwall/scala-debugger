package org.scaladebugger.api.profiles.pure.info

import java.util

import com.sun.jdi._
import org.scaladebugger.api.profiles.traits.info.ValueInfoProfile
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSpec, Matchers, ParallelTestExecution}

class PureArrayInfoProfileSpec extends FunSpec with Matchers
  with ParallelTestExecution with MockFactory
{
  private val mockVirtualMachine = mock[VirtualMachine]
  private val mockStackFrame = mock[StackFrame]
  private val mockArrayReference = mock[ArrayReference]
  private val pureArrayInfoProfile = new PureArrayInfoProfile(
    mockStackFrame,
    mockArrayReference
  )(mockVirtualMachine)

  describe("PureArrayInfoProfile") {
    describe("#length") {
      it("should return the length provided by the array reference") {
        val expected = 999

        (mockArrayReference.length _).expects().returning(expected).once()

        val actual = pureArrayInfoProfile.length

        actual should be (expected)
      }
    }

    describe("#getUnsafeValue") {
      it("should return a new value profile wrapping the value at the position") {
        val expected = mock[ValueInfoProfile]
        val mockValue = mock[Value]

        val pureArrayInfoProfile = new PureArrayInfoProfile(
          mockStackFrame,
          mockArrayReference
        )(mockVirtualMachine) {
          override protected def newValueProfile(value: Value): ValueInfoProfile = {
            value should be (mockValue)
            expected
          }
        }

        (mockArrayReference.getValue _).expects(0).returning(mockValue).once()
        val actual = pureArrayInfoProfile.getUnsafeValue(0)

        actual should be (expected)
      }
    }

    describe("#getUnsafeValues(index, totalElements)") {
      it("should return value profiles wrapping the return values") {
        val expected = Seq(mock[ValueInfoProfile])
        val mockValues = Seq(mock[Value])

        val pureArrayInfoProfile = new PureArrayInfoProfile(
          mockStackFrame,
          mockArrayReference
        )(mockVirtualMachine) {
          override protected def newValueProfile(value: Value): ValueInfoProfile = {
            value should be (mockValues.head)
            expected.head
          }
        }

        import scala.collection.JavaConverters._
        (mockArrayReference.getValues(_: Int, _: Int)).expects(0, 0)
          .returning(mockValues.asJava).once()
        val actual = pureArrayInfoProfile.getUnsafeValues(0, 0)

        actual should be (expected)
      }
    }

    describe("#getUnsafeValues") {
      it("should return value profiles wrapping the return values") {
        val expected = Seq(mock[ValueInfoProfile])
        val mockValues = Seq(mock[Value])

        val pureArrayInfoProfile = new PureArrayInfoProfile(
          mockStackFrame,
          mockArrayReference
        )(mockVirtualMachine) {
          override protected def newValueProfile(value: Value): ValueInfoProfile = {
            value should be (mockValues.head)
            expected.head
          }
        }

        import scala.collection.JavaConverters._
        (mockArrayReference.getValues: Function0[java.util.List[Value]]).expects()
          .returning(mockValues.asJava).once()
        val actual = pureArrayInfoProfile.getUnsafeValues

        actual should be (expected)
      }
    }

    describe("#setUnsafeValue") {
      it("should set and return the provided value at the specified position") {
        val expected = "some value"
        val index = 999
        val mockValue = mock[StringReference]

        (mockVirtualMachine.mirrorOf(_: String)).expects(expected)
          .returning(mockValue).once()
        (mockArrayReference.setValue _).expects(index, mockValue).once()

        val actual = pureArrayInfoProfile.setUnsafeValue(index, expected)

        actual should be (expected)
      }
    }

    describe("#setUnsafeValues(index, values, srcIndex, totalElements)") {
      it("should set and return the values starting from the src index to total elements") {
        val expected: Seq[AnyVal] = true :: 0.5f :: Nil

        val index = 999
        val values = "some value" +: expected
        val srcIndex = 1
        val totalElements = 2

        val mockStringReference = mock[StringReference]
        val mockBooleanValue = mock[BooleanValue]
        val mockFloatValue = mock[FloatValue]
        val mockValues = Seq(mockStringReference, mockBooleanValue, mockFloatValue)
        (mockVirtualMachine.mirrorOf(_: String)).expects(values(0).asInstanceOf[String])
          .returning(mockStringReference).once()
        (mockVirtualMachine.mirrorOf(_: Boolean)).expects(expected(0).asInstanceOf[Boolean])
          .returning(mockBooleanValue).once()
        (mockVirtualMachine.mirrorOf(_: Float)).expects(expected(1).asInstanceOf[Float])
          .returning(mockFloatValue).once()

        val mockSetValues = mockFunction[Int, java.util.List[_ <: Value], Int, Int, Unit]
        val testArrayReference = new TestArrayReference {
          override def setValues(i: Int, list: util.List[_ <: Value], i1: Int, i2: Int): Unit =
            mockSetValues(i, list, i1, i2)
        }

        import scala.collection.JavaConverters._
        mockSetValues.expects(index, mockValues.asJava, srcIndex, totalElements).once()
        val pureArrayInfoProfile = new PureArrayInfoProfile(
          mockStackFrame,
          testArrayReference
        )(mockVirtualMachine)

        val actual = pureArrayInfoProfile.setUnsafeValues(
          index,
          values,
          srcIndex,
          totalElements
        )

        actual should be (expected)
      }
    }

    describe("#setUnsafeValues(values)") {
      it("should set and return the provided values") {
        val expected = Seq("some value", 3, 0.5f)

        val mockStringReference = mock[StringReference]
        val mockIntegerValue = mock[IntegerValue]
        val mockFloatValue = mock[FloatValue]
        val mockValues = Seq(mockStringReference, mockIntegerValue, mockFloatValue)
        (mockVirtualMachine.mirrorOf(_: String)).expects(expected(0).asInstanceOf[String])
          .returning(mockStringReference).once()
        (mockVirtualMachine.mirrorOf(_: Int)).expects(expected(1).asInstanceOf[Int])
          .returning(mockIntegerValue).once()
        (mockVirtualMachine.mirrorOf(_: Float)).expects(expected(2).asInstanceOf[Float])
          .returning(mockFloatValue).once()

        val mockSetValues = mockFunction[java.util.List[_ <: Value], Unit]
        val testArrayReference = new TestArrayReference {
          override def setValues(list: util.List[_ <: Value]): Unit = mockSetValues(list)
        }

        import scala.collection.JavaConverters._
        mockSetValues.expects(mockValues.asJava).once()
        val pureArrayInfoProfile = new PureArrayInfoProfile(
          mockStackFrame,
          testArrayReference
        )(mockVirtualMachine)

        val actual = pureArrayInfoProfile.setUnsafeValues(expected)

        actual should be (expected)
      }
    }
  }

  // NOTE: This class exists so we can inject mock methods as ScalaMock has
  //       a bug where it cannot mock methods with generics
  //
  //       See https://github.com/paulbutcher/ScalaMock/issues/94
  private class TestArrayReference extends ArrayReference {
    override def getValue(i: Int): Value = ???
    override def setValue(i: Int, value: Value): Unit = ???
    override def length(): Int = ???
    override def setValues(list: util.List[_ <: Value]): Unit = ???
    override def setValues(i: Int, list: util.List[_ <: Value], i1: Int, i2: Int): Unit = ???
    override def getValues: util.List[Value] = ???
    override def getValues(i: Int, i1: Int): util.List[Value] = ???
    override def getValue(field: Field): Value = ???
    override def setValue(field: Field, value: Value): Unit = ???
    override def owningThread(): ThreadReference = ???
    override def referringObjects(l: Long): util.List[ObjectReference] = ???
    override def referenceType(): ReferenceType = ???
    override def uniqueID(): Long = ???
    override def invokeMethod(threadReference: ThreadReference, method: Method, list: util.List[_ <: Value], i: Int): Value = ???
    override def disableCollection(): Unit = ???
    override def enableCollection(): Unit = ???
    override def isCollected: Boolean = ???
    override def getValues(list: util.List[_ <: Field]): util.Map[Field, Value] = ???
    override def waitingThreads(): util.List[ThreadReference] = ???
    override def entryCount(): Int = ???
    override def `type`(): Type = ???
    override def virtualMachine(): VirtualMachine = ???
  }
}
