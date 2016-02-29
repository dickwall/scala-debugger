package org.scaladebugger.api.lowlevel.monitors
import acyclic.file

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSpec, Matchers, ParallelTestExecution}
import org.scaladebugger.api.lowlevel.requests.JDIRequestArgument
import test.TestMonitorWaitedManager

import scala.util.Success

class MonitorWaitedManagerSpec extends FunSpec with Matchers
  with ParallelTestExecution with MockFactory
{
  private val TestRequestId = java.util.UUID.randomUUID().toString
  private val mockMonitorWaitedManager = mock[MonitorWaitedManager]
  private val testMonitorWaitedManager = new TestMonitorWaitedManager(
    mockMonitorWaitedManager
  ) {
    override protected def newRequestId(): String = TestRequestId
  }

  describe("MonitorWaitedManager") {
    describe("#createMonitorWaitedRequest") {
      it("should invoke createMonitorWaitedRequestWithId") {
        val expected = Success(TestRequestId)
        val testExtraArguments = Seq(stub[JDIRequestArgument])

        (mockMonitorWaitedManager.createMonitorWaitedRequestWithId _)
          .expects(TestRequestId, testExtraArguments)
          .returning(expected).once()

        val actual = testMonitorWaitedManager.createMonitorWaitedRequest(
          testExtraArguments: _*
        )

        actual should be (expected)
      }
    }

    describe("#createMonitorWaitedRequestFromInfo") {
      it("should invoke createMonitorWaitedRequestWithId") {
        val expected = Success(TestRequestId)
        val testExtraArguments = Seq(stub[JDIRequestArgument])

        (mockMonitorWaitedManager.createMonitorWaitedRequestWithId _)
          .expects(TestRequestId, testExtraArguments)
          .returning(expected).once()

        val info = MonitorWaitedRequestInfo(
          TestRequestId,
          testExtraArguments
        )
        val actual = testMonitorWaitedManager.createMonitorWaitedRequestFromInfo(info)

        actual should be(expected)
      }
    }
  }
}
