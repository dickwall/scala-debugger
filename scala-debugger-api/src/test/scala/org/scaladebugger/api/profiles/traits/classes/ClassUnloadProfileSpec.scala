package org.scaladebugger.api.profiles.traits.classes
import acyclic.file

import com.sun.jdi.event.ClassUnloadEvent
import org.scaladebugger.api.lowlevel.classes.ClassUnloadRequestInfo
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSpec, Matchers, ParallelTestExecution}
import org.scaladebugger.api.lowlevel.JDIArgument
import org.scaladebugger.api.lowlevel.events.data.JDIEventDataResult
import org.scaladebugger.api.pipelines.Pipeline
import org.scaladebugger.api.pipelines.Pipeline.IdentityPipeline

import scala.util.{Failure, Success, Try}

class ClassUnloadProfileSpec extends FunSpec with Matchers
  with ParallelTestExecution with MockFactory
{
  private val TestThrowable = new Throwable

  // Pipeline that is parent to the one that just streams the event
  private val TestPipelineWithData = Pipeline.newPipeline(
    classOf[ClassUnloadProfile#ClassUnloadEventAndData]
  )

  private val successClassUnloadProfile = new Object with ClassUnloadProfile {
    override def onClassUnloadWithData(
      extraArguments: JDIArgument*
    ): Try[IdentityPipeline[ClassUnloadEventAndData]] = {
      Success(TestPipelineWithData)
    }

    override def classUnloadRequests: Seq[ClassUnloadRequestInfo] = ???
  }

  private val failClassUnloadProfile = new Object with ClassUnloadProfile {
    override def onClassUnloadWithData(
      extraArguments: JDIArgument*
    ): Try[IdentityPipeline[ClassUnloadEventAndData]] = {
      Failure(TestThrowable)
    }

    override def classUnloadRequests: Seq[ClassUnloadRequestInfo] = ???
  }

  describe("ClassUnloadProfile") {
    describe("#onClassUnload") {
      it("should return a pipeline with the event data results filtered out") {
        val expected = mock[ClassUnloadEvent]

        // Data to be run through pipeline
        val data = (expected, Seq(mock[JDIEventDataResult]))

        var actual: ClassUnloadEvent = null
        successClassUnloadProfile.onClassUnload().get.foreach(actual = _)

        // Funnel the data through the parent pipeline that contains data to
        // demonstrate that the pipeline with just the event is merely a
        // mapping on top of the pipeline containing the data
        TestPipelineWithData.process(data)

        actual should be (expected)
      }

      it("should capture any exception as a failure") {
        val expected = TestThrowable

        // Data to be run through pipeline
        val data = (mock[ClassUnloadEvent], Seq(mock[JDIEventDataResult]))

        var actual: Throwable = null
        failClassUnloadProfile.onClassUnload().failed.foreach(actual = _)

        actual should be (expected)
      }
    }

    describe("#onUnsafeClassUnload") {
      it("should return a pipeline of events if successful") {
        val expected = mock[ClassUnloadEvent]

        // Data to be run through pipeline
        val data = (expected, Seq(mock[JDIEventDataResult]))

        var actual: ClassUnloadEvent = null
        successClassUnloadProfile.onUnsafeClassUnload().foreach(actual = _)

        // Funnel the data through the parent pipeline that contains data to
        // demonstrate that the pipeline with just the event is merely a
        // mapping on top of the pipeline containing the data
        TestPipelineWithData.process(data)

        actual should be (expected)
      }

      it("should throw the exception if unsuccessful") {
        intercept[Throwable] {
          failClassUnloadProfile.onUnsafeClassUnload()
        }
      }
    }

    describe("#onUnsafeClassUnloadWithData") {
      it("should return a pipeline of events and data if successful") {
        // Data to be run through pipeline
        val expected = (mock[ClassUnloadEvent], Seq(mock[JDIEventDataResult]))

        var actual: (ClassUnloadEvent, Seq[JDIEventDataResult]) = null
        successClassUnloadProfile
          .onUnsafeClassUnloadWithData()
          .foreach(actual = _)

        // Funnel the data through the parent pipeline that contains data to
        // demonstrate that the pipeline with just the event is merely a
        // mapping on top of the pipeline containing the data
        TestPipelineWithData.process(expected)

        actual should be (expected)
      }

      it("should throw the exception if unsuccessful") {
        intercept[Throwable] {
          failClassUnloadProfile.onUnsafeClassUnloadWithData()
        }
      }
    }
  }
}

