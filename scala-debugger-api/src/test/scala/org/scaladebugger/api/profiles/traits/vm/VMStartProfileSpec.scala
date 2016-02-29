package org.scaladebugger.api.profiles.traits.vm
import acyclic.file

import com.sun.jdi.event.VMStartEvent
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSpec, Matchers, ParallelTestExecution}
import org.scaladebugger.api.lowlevel.JDIArgument
import org.scaladebugger.api.lowlevel.events.data.JDIEventDataResult
import org.scaladebugger.api.pipelines.Pipeline
import org.scaladebugger.api.pipelines.Pipeline.IdentityPipeline

import scala.util.{Failure, Success, Try}

class VMStartProfileSpec extends FunSpec with Matchers
  with ParallelTestExecution with MockFactory
{
  private val TestThrowable = new Throwable

  // Pipeline that is parent to the one that just streams the event
  private val TestPipelineWithData = Pipeline.newPipeline(
    classOf[VMStartProfile#VMStartEventAndData]
  )

  private val successVMStartProfile = new Object with VMStartProfile {
    override def onVMStartWithData(
      extraArguments: JDIArgument*
    ): Try[IdentityPipeline[VMStartEventAndData]] = {
      Success(TestPipelineWithData)
    }
  }

  private val failVMStartProfile = new Object with VMStartProfile {
    override def onVMStartWithData(
      extraArguments: JDIArgument*
    ): Try[IdentityPipeline[VMStartEventAndData]] = {
      Failure(TestThrowable)
    }
  }

  describe("VMStartProfile") {
    describe("#onVMStart") {
      it("should return a pipeline with the event data results filtered out") {
        val expected = mock[VMStartEvent]

        // Data to be run through pipeline
        val data = (expected, Seq(mock[JDIEventDataResult]))

        var actual: VMStartEvent = null
        successVMStartProfile.onVMStart().get.foreach(actual = _)

        // Funnel the data through the parent pipeline that contains data to
        // demonstrate that the pipeline with just the event is merely a
        // mapping on top of the pipeline containing the data
        TestPipelineWithData.process(data)

        actual should be (expected)
      }

      it("should capture any exception as a failure") {
        val expected = TestThrowable

        // Data to be run through pipeline
        val data = (mock[VMStartEvent], Seq(mock[JDIEventDataResult]))

        var actual: Throwable = null
        failVMStartProfile.onVMStart().failed.foreach(actual = _)

        actual should be (expected)
      }
    }

    describe("#onUnsafeVMStart") {
      it("should return a pipeline of events if successful") {
        val expected = mock[VMStartEvent]

        // Data to be run through pipeline
        val data = (expected, Seq(mock[JDIEventDataResult]))

        var actual: VMStartEvent = null
        successVMStartProfile.onUnsafeVMStart().foreach(actual = _)

        // Funnel the data through the parent pipeline that contains data to
        // demonstrate that the pipeline with just the event is merely a
        // mapping on top of the pipeline containing the data
        TestPipelineWithData.process(data)

        actual should be (expected)
      }

      it("should throw the exception if unsuccessful") {
        intercept[Throwable] {
          failVMStartProfile.onUnsafeVMStart()
        }
      }
    }

    describe("#onUnsafeVMStartWithData") {
      it("should return a pipeline of events and data if successful") {
        // Data to be run through pipeline
        val expected = (mock[VMStartEvent], Seq(mock[JDIEventDataResult]))

        var actual: (VMStartEvent, Seq[JDIEventDataResult]) = null
        successVMStartProfile
          .onUnsafeVMStartWithData()
          .foreach(actual = _)

        // Funnel the data through the parent pipeline that contains data to
        // demonstrate that the pipeline with just the event is merely a
        // mapping on top of the pipeline containing the data
        TestPipelineWithData.process(expected)

        actual should be (expected)
      }

      it("should throw the exception if unsuccessful") {
        intercept[Throwable] {
          failVMStartProfile.onUnsafeVMStartWithData()
        }
      }
    }
  }
}

