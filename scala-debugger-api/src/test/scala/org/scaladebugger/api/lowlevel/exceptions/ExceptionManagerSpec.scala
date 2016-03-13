package org.scaladebugger.api.lowlevel.exceptions
import acyclic.file

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSpec, Matchers, ParallelTestExecution}
import org.scaladebugger.api.lowlevel.requests.JDIRequestArgument
import test.TestExceptionManager

import scala.util.Success

class ExceptionManagerSpec extends FunSpec with Matchers
  with ParallelTestExecution with MockFactory
{
  private val TestRequestId = java.util.UUID.randomUUID().toString
  private val mockExceptionManager = mock[ExceptionManager]
  private val testExceptionManager = new TestExceptionManager(
    mockExceptionManager
  ) {
    override protected def newRequestId(): String = TestRequestId
  }

  describe("ExceptionManager") {
    describe("#createExceptionRequest") {
      it("should invoke createExceptionRequestWithId") {
        val expected = Success(TestRequestId)
        val testClassName = "some.class.name"
        val testNotifyCaught = true
        val testNotifyUncaught = false
        val testExtraArguments = Seq(stub[JDIRequestArgument])

        (mockExceptionManager.createExceptionRequestWithId _).expects(
          TestRequestId,
          testClassName,
          testNotifyCaught,
          testNotifyUncaught,
          testExtraArguments
        ).returning(expected).once()

        val actual = testExceptionManager.createExceptionRequest(
          testClassName,
          testNotifyCaught,
          testNotifyUncaught,
          testExtraArguments: _*
        )

        actual should be (expected)
      }
    }

    describe("#createCatchallExceptionRequest") {
      it("should invoke createCatchallExceptionRequestWithId") {
        val expected = Success(TestRequestId)
        val testNotifyCaught = true
        val testNotifyUncaught = false
        val testExtraArguments = Seq(stub[JDIRequestArgument])

        (mockExceptionManager.createCatchallExceptionRequestWithId _).expects(
          TestRequestId,
          testNotifyCaught,
          testNotifyUncaught,
          testExtraArguments
        ).returning(expected).once()

        val actual = testExceptionManager.createCatchallExceptionRequest(
          testNotifyCaught,
          testNotifyUncaught,
          testExtraArguments: _*
        )

        actual should be (expected)
      }
    }

    describe("#createExceptionRequestFromInfo") {
      it("should invoke createCatchallExceptionRequestWithId if the class name is null") {
        val expected = Success(TestRequestId)
        val testClassName = null
        val testNotifyCaught = true
        val testNotifyUncaught = false
        val testExtraArguments = Seq(stub[JDIRequestArgument])

        (mockExceptionManager.createCatchallExceptionRequestWithId _).expects(
          TestRequestId,
          testNotifyCaught,
          testNotifyUncaught,
          testExtraArguments
        ).returning(expected).once()

        val info = ExceptionRequestInfo(
          TestRequestId,
          testClassName,
          testNotifyCaught,
          testNotifyUncaught,
          testExtraArguments
        )
        val actual = testExceptionManager.createExceptionRequestFromInfo(info)

        actual should be(expected)
      }

      it("should invoke createExceptionRequestWithId if the class name is not null") {
        val expected = Success(TestRequestId)
        val testClassName = "some.class.name"
        val testNotifyCaught = true
        val testNotifyUncaught = false
        val testExtraArguments = Seq(stub[JDIRequestArgument])

        (mockExceptionManager.createExceptionRequestWithId _).expects(
          TestRequestId,
          testClassName,
          testNotifyCaught,
          testNotifyUncaught,
          testExtraArguments
        ).returning(expected).once()

        val info = ExceptionRequestInfo(
          TestRequestId,
          testClassName,
          testNotifyCaught,
          testNotifyUncaught,
          testExtraArguments
        )
        val actual = testExceptionManager.createExceptionRequestFromInfo(info)

        actual should be(expected)
      }
    }
  }
}
