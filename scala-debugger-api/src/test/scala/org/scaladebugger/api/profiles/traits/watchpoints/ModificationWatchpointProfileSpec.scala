package org.scaladebugger.api.profiles.traits.watchpoints
import acyclic.file

import com.sun.jdi.event.ModificationWatchpointEvent
import org.scaladebugger.api.lowlevel.watchpoints.ModificationWatchpointRequestInfo
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSpec, Matchers, ParallelTestExecution}
import org.scaladebugger.api.lowlevel.JDIArgument
import org.scaladebugger.api.lowlevel.events.data.JDIEventDataResult
import org.scaladebugger.api.pipelines.Pipeline
import org.scaladebugger.api.pipelines.Pipeline.IdentityPipeline

import scala.util.{Failure, Success, Try}

class ModificationWatchpointProfileSpec extends FunSpec with Matchers
  with ParallelTestExecution with MockFactory
{
  private val TestThrowable = new Throwable

  // Pipeline that is parent to the one that just streams the event
  private val TestPipelineWithData = Pipeline.newPipeline(
    classOf[ModificationWatchpointProfile#ModificationWatchpointEventAndData]
  )

  private val successModificationWatchpointProfile = new Object with ModificationWatchpointProfile {
    override def onModificationWatchpointWithData(
      className: String,
      fieldName: String,
      extraArguments: JDIArgument*
    ): Try[IdentityPipeline[ModificationWatchpointEventAndData]] = {
      Success(TestPipelineWithData)
    }

    override def modificationWatchpointRequests: Seq[ModificationWatchpointRequestInfo] = ???
  }

  private val failModificationWatchpointProfile = new Object with ModificationWatchpointProfile {
    override def onModificationWatchpointWithData(
      className: String,
      fieldName: String,
      extraArguments: JDIArgument*
    ): Try[IdentityPipeline[ModificationWatchpointEventAndData]] = {
      Failure(TestThrowable)
    }

    override def modificationWatchpointRequests: Seq[ModificationWatchpointRequestInfo] = ???
  }

  describe("ModificationWatchpointProfile") {
    describe("#onModificationWatchpoint") {
      it("should return a pipeline with the event data results filtered out") {
        val expected = mock[ModificationWatchpointEvent]

        // Data to be run through pipeline
        val data = (expected, Seq(mock[JDIEventDataResult]))

        var actual: ModificationWatchpointEvent = null
        successModificationWatchpointProfile
          .onModificationWatchpoint("", "")
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
        failModificationWatchpointProfile
          .onModificationWatchpoint("", "")
          .failed
          .foreach(actual = _)

        actual should be (expected)
      }
    }

    describe("#onUnsafeModificationWatchpoint") {
      it("should return a pipeline with the event data results filtered out") {
        val expected = mock[ModificationWatchpointEvent]

        // Data to be run through pipeline
        val data = (expected, Seq(mock[JDIEventDataResult]))

        var actual: ModificationWatchpointEvent = null
        successModificationWatchpointProfile
          .onUnsafeModificationWatchpoint("", "")
          .foreach(actual = _)

        // Funnel the data through the parent pipeline that contains data to
        // demonstrate that the pipeline with just the event is merely a
        // mapping on top of the pipeline containing the data
        TestPipelineWithData.process(data)

        actual should be (expected)
      }

      it("should throw an error if it occurs") {
        intercept[Throwable] {
          failModificationWatchpointProfile.onUnsafeModificationWatchpoint("", "")
        }
      }
    }

    describe("#onUnsafeModificationWatchpointWithData") {
      it("should return a pipeline with the event data results") {
        // Data to be run through pipeline
        val expected = (mock[ModificationWatchpointEvent], Seq(mock[JDIEventDataResult]))

        var actual: (ModificationWatchpointEvent, Seq[JDIEventDataResult]) = null
        successModificationWatchpointProfile
          .onUnsafeModificationWatchpointWithData("", "")
          .foreach(actual = _)

        // Funnel the data through the parent pipeline that contains data to
        // demonstrate that the pipeline with just the event is merely a
        // mapping on top of the pipeline containing the data
        TestPipelineWithData.process(expected)

        actual should be (expected)
      }

      it("should throw an error if it occurs") {
        intercept[Throwable] {
          failModificationWatchpointProfile
            .onUnsafeModificationWatchpointWithData("", "")
        }
      }
    }
  }
}

