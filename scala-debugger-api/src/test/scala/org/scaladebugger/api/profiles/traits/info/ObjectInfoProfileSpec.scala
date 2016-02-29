package org.scaladebugger.api.profiles.traits.info

import org.scaladebugger.api.lowlevel.{InvokeNonVirtualArgument, InvokeSingleThreadedArgument, JDIArgument}
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSpec, Matchers, ParallelTestExecution}
import test.InfoTestClasses.TestObjectInfoProfile

class ObjectInfoProfileSpec extends FunSpec with Matchers
  with ParallelTestExecution with MockFactory
{
  describe("ObjectInfoProfile") {
    describe("#invoke(methodProfile, arguments, JDI arguments)") {
      it("should wrap the unsafe call in a Try") {
        val mockUnsafeMethod = mockFunction[
          MethodInfoProfile,
          Seq[Any],
          Seq[JDIArgument],
          ValueInfoProfile
        ]

        val objectInfoProfile = new TestObjectInfoProfile {
          override def unsafeInvoke(
            methodInfoProfile: MethodInfoProfile,
            arguments: Seq[Any],
            jdiArguments: JDIArgument*
          ): ValueInfoProfile = mockUnsafeMethod(
            methodInfoProfile,
            arguments,
            jdiArguments.toSeq
          )
        }

        val a1 = mock[MethodInfoProfile]
        val a2 = Seq(3, "test", new AnyRef)
        val a3 = Seq(InvokeSingleThreadedArgument, InvokeNonVirtualArgument)
        val r = mock[ValueInfoProfile]

        mockUnsafeMethod.expects(a1, a2, a3).returning(r).once()

        objectInfoProfile.invoke(a1, a2, a3: _*).get should be (r)
      }
    }

    describe("#invoke(methodName, arguments, JDI arguments)") {
      it("should infer parameter types from provided arguments") {
        val mockUnsafeMethod = mockFunction[
          String,
          Seq[String],
          Seq[Any],
          Seq[JDIArgument],
          ValueInfoProfile
          ]

        val objectInfoProfile = new TestObjectInfoProfile {
          override def unsafeInvoke(
            methodName: String,
            parameterTypeNames: Seq[String],
            arguments: Seq[Any],
            jdiArguments: JDIArgument*
          ): ValueInfoProfile = mockUnsafeMethod(
            methodName,
            parameterTypeNames,
            arguments,
            jdiArguments.toSeq
          )
        }

        val a1 = "some method name"
        val a2 = Seq(3, "test", new AnyRef)
        val a3 = Seq(InvokeSingleThreadedArgument, InvokeNonVirtualArgument)
        val r = mock[ValueInfoProfile]
        val t = Seq("java.lang.Integer", "java.lang.String", "java.lang.Object")

        mockUnsafeMethod.expects(a1, t, a2, a3).returning(r).once()

        objectInfoProfile.invoke(a1, a2, a3: _*).get should be(r)
      }
    }

    describe("#invoke(methodName, parameter types, arguments, JDI arguments)") {
      it("should wrap the unsafe call in a Try") {
        val mockUnsafeMethod = mockFunction[
          String,
          Seq[String],
          Seq[Any],
          Seq[JDIArgument],
          ValueInfoProfile
        ]

        val objectInfoProfile = new TestObjectInfoProfile {
          override def unsafeInvoke(
            methodName: String,
            parameterTypeNames: Seq[String],
            arguments: Seq[Any],
            jdiArguments: JDIArgument*
          ): ValueInfoProfile = mockUnsafeMethod(
            methodName,
            parameterTypeNames,
            arguments,
            jdiArguments.toSeq
          )
        }

        val a1 = "some method name"
        val a2 = Seq("some", "parameter", "types")
        val a3 = Seq(3, "test", new AnyRef)
        val a4 = Seq(InvokeSingleThreadedArgument, InvokeNonVirtualArgument)
        val r = mock[ValueInfoProfile]

        mockUnsafeMethod.expects(a1, a2, a3, a4).returning(r).once()

        objectInfoProfile.invoke(a1, a2, a3, a4: _*).get should be (r)
      }
    }

    describe("#unsafeInvoke") {
      it("should infer parameter types from provided arguments") {
        val mockUnsafeMethod = mockFunction[
          String,
          Seq[String],
          Seq[Any],
          Seq[JDIArgument],
          ValueInfoProfile
        ]

        val objectInfoProfile = new TestObjectInfoProfile {
          override def unsafeInvoke(
            methodName: String,
            parameterTypeNames: Seq[String],
            arguments: Seq[Any],
            jdiArguments: JDIArgument*
          ): ValueInfoProfile = mockUnsafeMethod(
            methodName,
            parameterTypeNames,
            arguments,
            jdiArguments.toSeq
          )
        }

        val a1 = "some method name"
        val a2 = Seq(3, "test", new AnyRef)
        val a3 = Seq(InvokeSingleThreadedArgument, InvokeNonVirtualArgument)
        val r = mock[ValueInfoProfile]
        val t = Seq("java.lang.Integer", "java.lang.String", "java.lang.Object")

        mockUnsafeMethod.expects(a1, t, a2, a3).returning(r).once()

        objectInfoProfile.unsafeInvoke(a1, a2, a3: _*) should be(r)
      }
    }

    describe("#fields") {
      it("should wrap the unsafe call in a Try") {
        val mockUnsafeMethod = mockFunction[Seq[VariableInfoProfile]]

        val objectInfoProfile = new TestObjectInfoProfile {
          override def unsafeFields: Seq[VariableInfoProfile] = mockUnsafeMethod()
        }

        val r = Seq(mock[VariableInfoProfile])
        mockUnsafeMethod.expects().returning(r).once()
        objectInfoProfile.fields.get should be (r)
      }
    }

    describe("#field") {
      it("should wrap the unsafe call in a Try") {
        val mockUnsafeMethod = mockFunction[String, VariableInfoProfile]

        val objectInfoProfile = new TestObjectInfoProfile {
          override def unsafeField(name: String): VariableInfoProfile =
            mockUnsafeMethod(name)
        }

        val a1 = "someName"
        val r = mock[VariableInfoProfile]
        mockUnsafeMethod.expects(a1).returning(r).once()
        objectInfoProfile.field(a1).get should be (r)
      }
    }

    describe("#methods") {
      it("should wrap the unsafe call in a Try") {
        val mockUnsafeMethod = mockFunction[Seq[MethodInfoProfile]]

        val objectInfoProfile = new TestObjectInfoProfile {
          override def unsafeMethods: Seq[MethodInfoProfile] = mockUnsafeMethod()
        }

        val r = Seq(mock[MethodInfoProfile])
        mockUnsafeMethod.expects().returning(r).once()
        objectInfoProfile.methods.get should be (r)
      }
    }

    describe("#method") {
      it("should wrap the unsafe call in a Try") {
        val mockUnsafeMethod = mockFunction[String, Seq[String], MethodInfoProfile]

        val objectInfoProfile = new TestObjectInfoProfile {
          override def unsafeMethod(
            name: String,
            parameterTypeNames: String*
          ): MethodInfoProfile = mockUnsafeMethod(name, parameterTypeNames)
        }

        val a1 = "someName"
        val a2 = Seq("param.type")
        val r = mock[MethodInfoProfile]
        mockUnsafeMethod.expects(a1, a2).returning(r).once()
        objectInfoProfile.method(a1, a2: _*).get should be (r)
      }
    }
  }
}
