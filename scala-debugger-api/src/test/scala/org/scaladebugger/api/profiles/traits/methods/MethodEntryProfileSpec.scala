package org.scaladebugger.api.profiles.traits.methods
import acyclic.file

import com.sun.jdi.event.MethodEntryEvent
import org.scaladebugger.api.lowlevel.methods.MethodEntryRequestInfo
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSpec, Matchers, ParallelTestExecution}
import org.scaladebugger.api.lowlevel.JDIArgument
import org.scaladebugger.api.lowlevel.events.data.JDIEventDataResult
import org.scaladebugger.api.pipelines.Pipeline
import org.scaladebugger.api.pipelines.Pipeline.IdentityPipeline

import scala.util.{Failure, Success, Try}

class MethodEntryProfileSpec extends FunSpec with Matchers
  with ParallelTestExecution with MockFactory
{
  private val TestThrowable = new Throwable

  // Pipeline that is parent to the one that just streams the event
  private val TestPipelineWithData = Pipeline.newPipeline(
    classOf[MethodEntryProfile#MethodEntryEventAndData]
  )

  private val successMethodEntryProfile = new Object with MethodEntryProfile {
    override def onMethodEntryWithData(
      className: String,
      methodName: String,
      extraArguments: JDIArgument*
    ): Try[IdentityPipeline[MethodEntryEventAndData]] = {
      Success(TestPipelineWithData)
    }

    override def methodEntryRequests: Seq[MethodEntryRequestInfo] = ???
  }

  private val failMethodEntryProfile = new Object with MethodEntryProfile {
    override def onMethodEntryWithData(
      className: String,
      methodName: String,
      extraArguments: JDIArgument*
    ): Try[IdentityPipeline[MethodEntryEventAndData]] = {
      Failure(TestThrowable)
    }

    override def methodEntryRequests: Seq[MethodEntryRequestInfo] = ???
  }

  describe("MethodEntryProfile") {
    describe("#onMethodEntry") {
      it("should return a pipeline with the event data results filtered out") {
        val expected = mock[MethodEntryEvent]

        // Data to be run through pipeline
        val data = (expected, Seq(mock[JDIEventDataResult]))

        var actual: MethodEntryEvent = null
        successMethodEntryProfile.onMethodEntry("", "").get.foreach(actual = _)

        // Funnel the data through the parent pipeline that contains data to
        // demonstrate that the pipeline with just the event is merely a
        // mapping on top of the pipeline containing the data
        TestPipelineWithData.process(data)

        actual should be (expected)
      }

      it("should capture any exception as a failure") {
        val expected = TestThrowable

        // Data to be run through pipeline
        val data = (mock[MethodEntryEvent], Seq(mock[JDIEventDataResult]))

        var actual: Throwable = null
        failMethodEntryProfile.onMethodEntry("", "").failed.foreach(actual = _)

        actual should be (expected)
      }
    }

    describe("#onUnsafeMethodEntry") {
      it("should return a pipeline of events if successful") {
        val expected = mock[MethodEntryEvent]

        // Data to be run through pipeline
        val data = (expected, Seq(mock[JDIEventDataResult]))

        var actual: MethodEntryEvent = null
        successMethodEntryProfile
          .onUnsafeMethodEntry("", "")
          .foreach(actual = _)

        // Funnel the data through the parent pipeline that contains data to
        // demonstrate that the pipeline with just the event is merely a
        // mapping on top of the pipeline containing the data
        TestPipelineWithData.process(data)

        actual should be (expected)
      }

      it("should throw the exception if unsuccessful") {
        intercept[Throwable] {
          failMethodEntryProfile.onUnsafeMethodEntry("", "")
        }
      }
    }

    describe("#onUnsafeMethodEntryWithData") {
      it("should return a pipeline of events and data if successful") {
        // Data to be run through pipeline
        val expected = (mock[MethodEntryEvent], Seq(mock[JDIEventDataResult]))

        var actual: (MethodEntryEvent, Seq[JDIEventDataResult]) = null
        successMethodEntryProfile
          .onUnsafeMethodEntryWithData("", "")
          .foreach(actual = _)

        // Funnel the data through the parent pipeline that contains data to
        // demonstrate that the pipeline with just the event is merely a
        // mapping on top of the pipeline containing the data
        TestPipelineWithData.process(expected)

        actual should be (expected)
      }

      it("should throw the exception if unsuccessful") {
        intercept[Throwable] {
          failMethodEntryProfile.onUnsafeMethodEntryWithData("", "")
        }
      }
    }
  }
}

