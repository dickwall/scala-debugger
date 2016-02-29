package org.scaladebugger.api.profiles.traits.breakpoints
import acyclic.file

import com.sun.jdi.event.BreakpointEvent
import org.scaladebugger.api.lowlevel.breakpoints.BreakpointRequestInfo
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSpec, Matchers, ParallelTestExecution}
import org.scaladebugger.api.lowlevel.JDIArgument
import org.scaladebugger.api.lowlevel.events.data.JDIEventDataResult
import org.scaladebugger.api.pipelines.Pipeline
import org.scaladebugger.api.pipelines.Pipeline.IdentityPipeline

import scala.util.{Failure, Success, Try}

class BreakpointProfileSpec extends FunSpec with Matchers
  with ParallelTestExecution with MockFactory
{
  private val TestThrowable = new Throwable

  // Pipeline that is parent to the one that just streams the event
  private val TestPipelineWithData = Pipeline.newPipeline(
    classOf[BreakpointProfile#BreakpointEventAndData]
  )

  private val successBreakpointProfile = new Object with BreakpointProfile {
    override def onBreakpointWithData(
      fileName: String,
      lineNumber: Int,
      extraArguments: JDIArgument*
    ): Try[IdentityPipeline[BreakpointEventAndData]] = {
      Success(TestPipelineWithData)
    }

    override def breakpointRequests: Seq[BreakpointRequestInfo] = ???
  }

  private val failBreakpointProfile = new Object with BreakpointProfile {
    override def onBreakpointWithData(
      fileName: String,
      lineNumber: Int,
      extraArguments: JDIArgument*
    ): Try[IdentityPipeline[BreakpointEventAndData]] = {
      Failure(TestThrowable)
    }

    override def breakpointRequests: Seq[BreakpointRequestInfo] = ???
  }

  describe("BreakpointProfile") {
    describe("#onBreakpoint") {
      it("should return a pipeline with the event data results filtered out") {
        val expected = mock[BreakpointEvent]

        // Data to be run through pipeline
        val data = (expected, Seq(mock[JDIEventDataResult]))

        var actual: BreakpointEvent = null
        successBreakpointProfile.onBreakpoint("", 0).get.foreach(actual = _)

        // Funnel the data through the parent pipeline that contains data to
        // demonstrate that the pipeline with just the event is merely a
        // mapping on top of the pipeline containing the data
        TestPipelineWithData.process(data)

        actual should be (expected)
      }

      it("should capture any exception as a failure") {
        val expected = TestThrowable

        // Data to be run through pipeline
        val data = (mock[BreakpointEvent], Seq(mock[JDIEventDataResult]))

        var actual: Throwable = null
        failBreakpointProfile.onBreakpoint("", 0).failed.foreach(actual = _)

        actual should be (expected)
      }
    }

    describe("#onUnsafeBreakpoint") {
      it("should return a pipeline of events if successful") {
        val expected = mock[BreakpointEvent]

        // Data to be run through pipeline
        val data = (expected, Seq(mock[JDIEventDataResult]))

        var actual: BreakpointEvent = null
        successBreakpointProfile.onUnsafeBreakpoint("", 0).foreach(actual = _)

        // Funnel the data through the parent pipeline that contains data to
        // demonstrate that the pipeline with just the event is merely a
        // mapping on top of the pipeline containing the data
        TestPipelineWithData.process(data)

        actual should be (expected)
      }

      it("should throw the exception if unsuccessful") {
        intercept[Throwable] {
          failBreakpointProfile.onUnsafeBreakpoint("", 0)
        }
      }
    }

    describe("#onUnsafeBreakpointWithData") {
      it("should return a pipeline of events and data if successful") {
        // Data to be run through pipeline
        val expected = (mock[BreakpointEvent], Seq(mock[JDIEventDataResult]))

        var actual: (BreakpointEvent, Seq[JDIEventDataResult]) = null
        successBreakpointProfile
          .onUnsafeBreakpointWithData("", 0)
          .foreach(actual = _)

        // Funnel the data through the parent pipeline that contains data to
        // demonstrate that the pipeline with just the event is merely a
        // mapping on top of the pipeline containing the data
        TestPipelineWithData.process(expected)

        actual should be (expected)
      }

      it("should throw the exception if unsuccessful") {
        intercept[Throwable] {
          failBreakpointProfile.onUnsafeBreakpointWithData("", 0)
        }
      }
    }
  }
}
