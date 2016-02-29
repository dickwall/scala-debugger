package org.scaladebugger.api.profiles.traits.watchpoints
import acyclic.file

import com.sun.jdi.event.AccessWatchpointEvent
import org.scaladebugger.api.lowlevel.watchpoints.AccessWatchpointRequestInfo
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSpec, Matchers, ParallelTestExecution}
import org.scaladebugger.api.lowlevel.JDIArgument
import org.scaladebugger.api.lowlevel.events.data.JDIEventDataResult
import org.scaladebugger.api.pipelines.Pipeline
import org.scaladebugger.api.pipelines.Pipeline.IdentityPipeline

import scala.util.{Failure, Success, Try}

class AccessWatchpointProfileSpec extends FunSpec with Matchers
  with ParallelTestExecution with MockFactory
{
  private val TestThrowable = new Throwable

  // Pipeline that is parent to the one that just streams the event
  private val TestPipelineWithData = Pipeline.newPipeline(
    classOf[AccessWatchpointProfile#AccessWatchpointEventAndData]
  )

  private val successAccessWatchpointProfile = new Object with AccessWatchpointProfile {
    override def onAccessWatchpointWithData(
      className: String,
      fieldName: String,
      extraArguments: JDIArgument*
    ): Try[IdentityPipeline[AccessWatchpointEventAndData]] = {
      Success(TestPipelineWithData)
    }

    override def accessWatchpointRequests: Seq[AccessWatchpointRequestInfo] = ???
  }

  private val failAccessWatchpointProfile = new Object with AccessWatchpointProfile {
    override def onAccessWatchpointWithData(
      className: String,
      fieldName: String,
      extraArguments: JDIArgument*
    ): Try[IdentityPipeline[AccessWatchpointEventAndData]] = {
      Failure(TestThrowable)
    }

    override def accessWatchpointRequests: Seq[AccessWatchpointRequestInfo] = ???
  }

  describe("AccessWatchpointProfile") {
    describe("#onAccessWatchpoint") {
      it("should return a pipeline with the event data results filtered out") {
        val expected = mock[AccessWatchpointEvent]

        // Data to be run through pipeline
        val data = (expected, Seq(mock[JDIEventDataResult]))

        var actual: AccessWatchpointEvent = null
        successAccessWatchpointProfile
          .onAccessWatchpoint("", "")
          .get
          .foreach(actual = _)

        // Funnel the data through the parent pipeline that contains data to
        // demonstrate that the pipeline with just the event is merely a
        // mapping on top of the pipeline containing the data
        TestPipelineWithData.process(data)

        actual should be (expected)
      }

      it("should capture any error as a failure") {
        val expected = TestThrowable

        var actual: Throwable = null
        failAccessWatchpointProfile
          .onAccessWatchpoint("", "")
          .failed
          .foreach(actual = _)

        actual should be (expected)
      }
    }

    describe("#onUnsafeAccessWatchpoint") {
      it("should return a pipeline with the event data results filtered out") {
        val expected = mock[AccessWatchpointEvent]

        // Data to be run through pipeline
        val data = (expected, Seq(mock[JDIEventDataResult]))

        var actual: AccessWatchpointEvent = null
        successAccessWatchpointProfile
          .onUnsafeAccessWatchpoint("", "")
          .foreach(actual = _)

        // Funnel the data through the parent pipeline that contains data to
        // demonstrate that the pipeline with just the event is merely a
        // mapping on top of the pipeline containing the data
        TestPipelineWithData.process(data)

        actual should be (expected)
      }

      it("should throw an error if it occurs") {
        intercept[Throwable] {
          failAccessWatchpointProfile.onUnsafeAccessWatchpoint("", "")
        }
      }
    }

    describe("#onUnsafeAccessWatchpointWithData") {
      it("should return a pipeline with the event data results") {
        // Data to be run through pipeline
        val expected = (mock[AccessWatchpointEvent], Seq(mock[JDIEventDataResult]))

        var actual: (AccessWatchpointEvent, Seq[JDIEventDataResult]) = null
        successAccessWatchpointProfile
          .onUnsafeAccessWatchpointWithData("", "")
          .foreach(actual = _)

        // Funnel the data through the parent pipeline that contains data to
        // demonstrate that the pipeline with just the event is merely a
        // mapping on top of the pipeline containing the data
        TestPipelineWithData.process(expected)

        actual should be (expected)
      }

      it("should throw an error if it occurs") {
        intercept[Throwable] {
          failAccessWatchpointProfile
            .onUnsafeAccessWatchpointWithData("", "")
        }
      }
    }
  }
}

