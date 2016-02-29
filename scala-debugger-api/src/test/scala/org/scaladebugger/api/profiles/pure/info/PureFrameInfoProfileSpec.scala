package org.scaladebugger.api.profiles.pure.info

import com.sun.jdi._
import org.scaladebugger.api.profiles.traits.info.{VariableInfoProfile, ObjectInfoProfile, ThreadInfoProfile}
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSpec, Matchers, ParallelTestExecution}

class PureFrameInfoProfileSpec extends FunSpec with Matchers
  with ParallelTestExecution with MockFactory
{
  private val mockNewLocalVariableProfile = mockFunction[LocalVariable, VariableInfoProfile]
  private val mockNewObjectProfile = mockFunction[ObjectReference, ObjectInfoProfile]
  private val mockNewThreadProfile = mockFunction[ThreadReference, ThreadInfoProfile]

  private val mockStackFrame = mock[StackFrame]
  private val pureFrameInfoProfile = new PureFrameInfoProfile(mockStackFrame) {
    override protected def newLocalVariableProfile(
      localVariable: LocalVariable
    ): VariableInfoProfile = mockNewLocalVariableProfile(localVariable)

    override protected def newObjectProfile(
      objectReference: ObjectReference
    ): ObjectInfoProfile = mockNewObjectProfile(objectReference)

    override protected def newThreadProfile(
      threadReference: ThreadReference
    ): ThreadInfoProfile = mockNewThreadProfile(threadReference)
  }

  describe("PureFrameInfoProfile") {
    describe("#withUnsafeThisObject") {
      it("should return the stack frame's 'this' object wrapped in a profile") {
        val expected = mock[ObjectInfoProfile]
        val mockObjectReference = mock[ObjectReference]

        // stackFrame.thisObject() fed into newObjectProfile
        (mockStackFrame.thisObject _).expects()
          .returning(mockObjectReference).once()

        // New object profile created once using helper method
        mockNewObjectProfile.expects(mockObjectReference)
          .returning(expected).once()

        val actual = pureFrameInfoProfile.withUnsafeThisObject
        actual should be (expected)
      }

      it("should use the same cached 'this' object profile") {
        val mockObjectProfile = mock[ObjectInfoProfile]

        // stackFrame.thisObject() fed into newObjectProfile
        (mockStackFrame.thisObject _).expects()
          .returning(mock[ObjectReference]).once()

        // New object profile created once using helper method
        mockNewObjectProfile.expects(*).returning(mockObjectProfile).once()

        pureFrameInfoProfile.withUnsafeThisObject should
          be (pureFrameInfoProfile.withUnsafeThisObject)
      }
    }

    describe("#withUnsafeCurrentThread") {
      it("should return the stack frame's thread wrapped in a profile") {
        val expected = mock[ThreadInfoProfile]
        val mockThreadReference = mock[ThreadReference]

        // stackFrame.thread() fed into newThreadProfile
        (mockStackFrame.thread _).expects()
          .returning(mockThreadReference).once()

        // New thread profile created once using helper method
        mockNewThreadProfile.expects(mockThreadReference)
          .returning(expected).once()

        val actual = pureFrameInfoProfile.withUnsafeCurrentThread
        actual should be (expected)
      }

      it("Should use the same cached thread profile") {
        val mockThreadProfile = mock[ThreadInfoProfile]

        // stackFrame.thread() fed into newThreadProfile
        (mockStackFrame.thread _).expects()
          .returning(mock[ThreadReference]).once()

        // New thread profile created once using helper method
        mockNewThreadProfile.expects(*).returning(mockThreadProfile).once()

        pureFrameInfoProfile.withUnsafeCurrentThread should
          be (pureFrameInfoProfile.withUnsafeCurrentThread)
      }
    }

    describe("#forUnsafeVariable") {
      it("should return a local variable wrapped in a profile if it exists") {
        val expected = mock[VariableInfoProfile]

        val name = "someName"
        val mockLocalVariable = mock[LocalVariable]

        // Match found in visible variable collection
        (mockStackFrame.visibleVariableByName _).expects(name)
          .returning(mockLocalVariable).once()
        mockNewLocalVariableProfile.expects(mockLocalVariable)
          .returning(expected).once()

        val actual = pureFrameInfoProfile.forUnsafeVariable(name)

        actual should be (expected)
      }

      it("should return a field wrapped in a profile in no local variable exists") {
        val expected = mock[VariableInfoProfile]

        val name = "someName"

        // No match found in visible variables, so return null
        (mockStackFrame.visibleVariableByName _).expects(name)
          .returning(null).once()

        // 'this' object profile is created and used
        val mockObjectProfile = mock[ObjectInfoProfile]
        (mockStackFrame.thisObject _).expects()
          .returning(mock[ObjectReference]).once()
        mockNewObjectProfile.expects(*).returning(mockObjectProfile).once()

        // unsafeField used to find object profile field
        (mockObjectProfile.unsafeField _).expects(name)
          .returning(expected).once()

        val actual = pureFrameInfoProfile.forUnsafeVariable(name)

        actual should be (expected)
      }

      it("should throw a NoSuchElement exception if no local variable or field matches") {
        val name = "someName"

        // No match found in visible variables, so return null
        (mockStackFrame.visibleVariableByName _).expects(name)
          .returning(null).once()

        // 'this' object profile is created and used
        val mockObjectProfile = mock[ObjectInfoProfile]
        (mockStackFrame.thisObject _).expects()
          .returning(mock[ObjectReference]).once()
        mockNewObjectProfile.expects(*).returning(mockObjectProfile).once()

        // unsafeField does not find a field and throws a NoSuchElement exception
        (mockObjectProfile.unsafeField _).expects(name)
          .throwing(new NoSuchElementException).once()

        intercept[NoSuchElementException] {
          pureFrameInfoProfile.forUnsafeVariable(name)
        }
      }
    }

    describe("#forUnsafeFieldVariables") {
      it("should return a collection of profiles wrapping 'this' object's fields") {
        val expected = Seq(mock[VariableInfoProfile])

        // 'this' object profile is created and used
        val mockObjectProfile = mock[ObjectInfoProfile]
        (mockStackFrame.thisObject _).expects()
          .returning(mock[ObjectReference]).once()
        mockNewObjectProfile.expects(*).returning(mockObjectProfile).once()

        // Field variable profiles are accessed
        (mockObjectProfile.unsafeFields _).expects().returning(expected).once()

        val actual = pureFrameInfoProfile.forUnsafeFieldVariables

        actual should be (expected)
      }
    }

    describe("#forUnsafeAllVariables") {
      it("should return a combination of local and field variables") {
        val fieldVariables = Seq(mock[VariableInfoProfile])
        val localVariables = Seq(mock[VariableInfoProfile])
        val expected = localVariables ++ fieldVariables

        val pureFrameInfoProfile = new PureFrameInfoProfile(mockStackFrame) {
          override def forUnsafeFieldVariables: Seq[VariableInfoProfile] =
            fieldVariables
          override def forUnsafeLocalVariables: Seq[VariableInfoProfile] =
            localVariables
        }

        val actual = pureFrameInfoProfile.forUnsafeAllVariables

        actual should be (expected)
      }
    }

    describe("#forUnsafeLocalVariables") {
      it("should return all visible variables wrapped in profiles") {
        val expected = Seq(mock[VariableInfoProfile])
        val mockLocalVariables = Seq(mock[LocalVariable])

        // Raw local variables accessed from stack frame
        import scala.collection.JavaConverters._
        (mockStackFrame.visibleVariables _).expects()
          .returning(mockLocalVariables.asJava).once()

        // Converted into profiles
        mockLocalVariables.zip(expected).foreach { case (lv, e) =>
          mockNewLocalVariableProfile.expects(lv).returning(e).once()
        }

        val actual = pureFrameInfoProfile.forUnsafeLocalVariables

        actual should be (expected)
      }
    }

    describe("#forUnsafeNonArguments") {
      it("should return only non-argument visible variable profiles") {
        val expected = Seq(mock[VariableInfoProfile])
        val other = Seq(mock[VariableInfoProfile])

        val pureFrameInfoProfile = new PureFrameInfoProfile(mockStackFrame) {
          override def forUnsafeLocalVariables: Seq[VariableInfoProfile] =
            expected ++ other
        }

        expected.foreach(v => (v.isArgument _).expects().returning(false).once())
        other.foreach(v => (v.isArgument _).expects().returning(true).once())

        val actual = pureFrameInfoProfile.forUnsafeNonArguments

        actual should be (expected)
      }
    }

    describe("#forUnsafeArguments") {
      it("should return only argument visible variable profiles") {
        val expected = Seq(mock[VariableInfoProfile])
        val other = Seq(mock[VariableInfoProfile])

        val pureFrameInfoProfile = new PureFrameInfoProfile(mockStackFrame) {
          override def forUnsafeLocalVariables: Seq[VariableInfoProfile] =
            expected ++ other
        }

        expected.foreach(v => (v.isArgument _).expects().returning(true).once())
        other.foreach(v => (v.isArgument _).expects().returning(false).once())

        val actual = pureFrameInfoProfile.forUnsafeArguments

        actual should be (expected)
      }
    }
  }
}
