package org.scaladebugger.api.profiles.pure.info

import com.sun.jdi._
import org.scaladebugger.api.profiles.traits.info.ValueInfoProfile
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSpec, Matchers, ParallelTestExecution}

class PureLocalVariableInfoProfileSpec extends FunSpec with Matchers
  with ParallelTestExecution with MockFactory
{
  private val mockVirtualMachine = mock[VirtualMachine]
  private val mockStackFrame = mock[StackFrame]
  private val mockLocalVariable = mock[LocalVariable]
  private val pureLocalVariableInfoProfile = new PureLocalVariableInfoProfile(
    mockStackFrame,
    mockLocalVariable
  )(mockVirtualMachine)

  describe("PureLocalVariableInfoProfile") {
    describe("#name") {
      it("should return the local variable's name") {
        val expected = "someName"

        (mockLocalVariable.name _).expects().returning(expected).once()

        val actual = pureLocalVariableInfoProfile.name

        actual should be (expected)
      }
    }

    describe("#isLocal") {
      it("should return true") {
        val expected = true

        val actual = pureLocalVariableInfoProfile.isLocal

        actual should be (expected)
      }
    }

    describe("#isArgument") {
      it("should return true if is an argument") {
        val expected = true

        (mockLocalVariable.isArgument _).expects().returning(true).once()

        val actual = pureLocalVariableInfoProfile.isArgument

        actual should be (expected)
      }

      it("should return false if is not an argument") {
        val expected = false

        (mockLocalVariable.isArgument _).expects().returning(false).once()

        val actual = pureLocalVariableInfoProfile.isArgument

        actual should be (expected)
      }
    }

    describe("#isField") {
      it("should return false") {
        val expected = false

        val actual = pureLocalVariableInfoProfile.isField

        actual should be (expected)
      }
    }

    describe("#setValue") {
      it("should set strings directly on the object") {
        val expected = "some value"

        // Mirror the local string remotely
        val mockStringReference = mock[StringReference]
        (mockVirtualMachine.mirrorOf(_: String)).expects(expected)
          .returning(mockStringReference).once()

        // Ensure setting value on stack frame is verified
        (mockStackFrame.setValue _)
          .expects(mockLocalVariable, mockStringReference)
          .once()

        pureLocalVariableInfoProfile.setValue(expected).get should be (expected)
      }

      it("should set primitive values directly on the object") {
        val expected = 3.toByte

        // Mirror the local string remotely
        val mockByteValue = mock[ByteValue]
        (mockVirtualMachine.mirrorOf(_: Byte)).expects(expected)
          .returning(mockByteValue).once()

        // Ensure setting value on stack frame is verified
        (mockStackFrame.setValue _)
          .expects(mockLocalVariable, mockByteValue)
          .once()

        pureLocalVariableInfoProfile.setValue(expected).get should be (expected)
      }
    }

    describe("#toUnsafeValue") {
      it("should return a wrapper around the value of the localVariable") {
        val expected = mock[ValueInfoProfile]
        val mockValue = mock[Value]

        // Retrieve the value from the stack frame
        (mockStackFrame.getValue _).expects(mockLocalVariable)
          .returning(mockValue).once()

        val mockNewValueProfile = mockFunction[Value, ValueInfoProfile]
        val pureLocalVariableInfoProfile = new PureLocalVariableInfoProfile(
          mockStackFrame,
          mockLocalVariable
        )(mockVirtualMachine) {
          override protected def newValueProfile(value: Value): ValueInfoProfile =
            mockNewValueProfile(value)
        }

        mockNewValueProfile.expects(mockValue).returning(expected).once()
        pureLocalVariableInfoProfile.toUnsafeValue should be (expected)
      }
    }
  }
}
