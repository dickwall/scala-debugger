package org.scaladebugger.api.profiles.traits.methods
import acyclic.file

import com.sun.jdi.event.MethodExitEvent
import org.scaladebugger.api.lowlevel.methods.MethodExitRequestInfo
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSpec, Matchers, ParallelTestExecution}
import org.scaladebugger.api.lowlevel.JDIArgument
import org.scaladebugger.api.lowlevel.events.data.JDIEventDataResult
import org.scaladebugger.api.pipelines.Pipeline
import org.scaladebugger.api.pipelines.Pipeline.IdentityPipeline

import scala.util.{Failure, Success, Try}

class MethodExitProfileSpec extends FunSpec with Matchers
  with ParallelTestExecution with MockFactory
{
  private val TestThrowable = new Throwable

  // Pipeline that is parent to the one that just streams the event
  private val TestPipelineWithData = Pipeline.newPipeline(
    classOf[MethodExitProfile#MethodExitEventAndData]
  )

  private val successMethodExitProfile = new Object with MethodExitProfile {
    override def onMethodExitWithData(
      className: String,
      methodName: String,
      extraArguments: JDIArgument*
    ): Try[IdentityPipeline[MethodExitEventAndData]] = {
      Success(TestPipelineWithData)
    }

    override def methodExitRequests: Seq[MethodExitRequestInfo] = ???
  }

  private val failMethodExitProfile = new Object with MethodExitProfile {
    override def onMethodExitWithData(
      className: String,
      methodName: String,
      extraArguments: JDIArgument*
    ): Try[IdentityPipeline[MethodExitEventAndData]] = {
      Failure(TestThrowable)
    }

    override def methodExitRequests: Seq[MethodExitRequestInfo] = ???
  }

  describe("MethodExitProfile") {
    describe("#onMethodExit") {
      it("should return a pipeline with the event data results filtered out") {
        val expected = mock[MethodExitEvent]

        // Data to be run through pipeline
        val data = (expected, Seq(mock[JDIEventDataResult]))

        var actual: MethodExitEvent = null
        successMethodExitProfile.onMethodExit("", "").get.foreach(actual = _)

        // Funnel the data through the parent pipeline that contains data to
        // demonstrate that the pipeline with just the event is merely a
        // mapping on top of the pipeline containing the data
        TestPipelineWithData.process(data)

        actual should be (expected)
      }

      it("should capture any exception as a failure") {
        val expected = TestThrowable

        // Data to be run through pipeline
        val data = (mock[MethodExitEvent], Seq(mock[JDIEventDataResult]))

        var actual: Throwable = null
        failMethodExitProfile.onMethodExit("", "").failed.foreach(actual = _)

        actual should be (expected)
      }
    }

    describe("#onUnsafeMethodExit") {
      it("should return a pipeline of events if successful") {
        val expected = mock[MethodExitEvent]

        // Data to be run through pipeline
        val data = (expected, Seq(mock[JDIEventDataResult]))

        var actual: MethodExitEvent = null
        successMethodExitProfile
          .onUnsafeMethodExit("", "")
          .foreach(actual = _)

        // Funnel the data through the parent pipeline that contains data to
        // demonstrate that the pipeline with just the event is merely a
        // mapping on top of the pipeline containing the data
        TestPipelineWithData.process(data)

        actual should be (expected)
      }

      it("should throw the exception if unsuccessful") {
        intercept[Throwable] {
          failMethodExitProfile.onUnsafeMethodExit("", "")
        }
      }
    }

    describe("#onUnsafeMethodExitWithData") {
      it("should return a pipeline of events and data if successful") {
        // Data to be run through pipeline
        val expected = (mock[MethodExitEvent], Seq(mock[JDIEventDataResult]))

        var actual: (MethodExitEvent, Seq[JDIEventDataResult]) = null
        successMethodExitProfile
          .onUnsafeMethodExitWithData("", "")
          .foreach(actual = _)

        // Funnel the data through the parent pipeline that contains data to
        // demonstrate that the pipeline with just the event is merely a
        // mapping on top of the pipeline containing the data
        TestPipelineWithData.process(expected)

        actual should be (expected)
      }

      it("should throw the exception if unsuccessful") {
        intercept[Throwable] {
          failMethodExitProfile.onUnsafeMethodExitWithData("", "")
        }
      }
    }
  }
}

