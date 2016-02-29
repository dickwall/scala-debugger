package org.scaladebugger.api.profiles.pure.breakpoints
import acyclic.file

import com.sun.jdi.event.Event
import org.scaladebugger.api.lowlevel.breakpoints.{BreakpointManager, BreakpointRequestInfo, PendingBreakpointSupportLike}
import org.scaladebugger.api.lowlevel.classes.ClassManager
import org.scaladebugger.api.lowlevel.events.EventManager
import org.scaladebugger.api.lowlevel.events.EventType.BreakpointEventType
import org.scaladebugger.api.lowlevel.events.data.JDIEventDataResult
import org.scaladebugger.api.lowlevel.events.filters.UniqueIdPropertyFilter
import org.scaladebugger.api.lowlevel.requests.JDIRequestArgument
import org.scaladebugger.api.lowlevel.requests.properties.UniqueIdProperty
import org.scaladebugger.api.pipelines.Pipeline
import org.scaladebugger.api.profiles.Constants
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSpec, Matchers, ParallelTestExecution}
import test.JDIMockHelpers

import scala.util.{Failure, Success}

class PureBreakpointProfileSpec extends FunSpec with Matchers
  with ParallelTestExecution with MockFactory with JDIMockHelpers
{
  private val TestRequestId = java.util.UUID.randomUUID().toString
  private val stubClassManager = stub[ClassManager]
  private val mockBreakpointManager = mock[BreakpointManager]
  private val mockEventManager = mock[EventManager]

  private val pureBreakpointProfile = new Object with PureBreakpointProfile {
    private var requestId: String = _
    def setRequestId(requestId: String): Unit = this.requestId = requestId

    // NOTE: If we set a specific request id, return that, otherwise use the
    //       default behavior
    override protected def newBreakpointRequestId(): String =
      if (requestId != null) requestId else super.newBreakpointRequestId()

    override protected val breakpointManager = mockBreakpointManager
    override protected val eventManager: EventManager = mockEventManager
  }

  describe("PureBreakpointProfile") {
    describe("#breakpointRequests") {
      it("should include all active requests") {
        val expected = Seq(
          BreakpointRequestInfo(TestRequestId, "some file", 999)
        )

        val mockBreakpointManager = mock[PendingBreakpointSupportLike]
        val pureBreakpointProfile = new Object with PureBreakpointProfile {
          override protected val breakpointManager = mockBreakpointManager
          override protected val eventManager: EventManager = mockEventManager
        }

        (mockBreakpointManager.breakpointRequestList _).expects()
          .returning(expected).once()

        (mockBreakpointManager.pendingBreakpointRequests _).expects()
          .returning(Nil).once()

        val actual = pureBreakpointProfile.breakpointRequests

        actual should be (expected)
      }

      it("should include pending requests if supported") {
        val expected = Seq(
          BreakpointRequestInfo(TestRequestId, "some file", 999)
        )

        val mockBreakpointManager = mock[PendingBreakpointSupportLike]
        val pureBreakpointProfile = new Object with PureBreakpointProfile {
          override protected val breakpointManager = mockBreakpointManager
          override protected val eventManager: EventManager = mockEventManager
        }

        (mockBreakpointManager.breakpointRequestList _).expects()
          .returning(Nil).once()

        (mockBreakpointManager.pendingBreakpointRequests _).expects()
          .returning(expected).once()

        val actual = pureBreakpointProfile.breakpointRequests

        actual should be (expected)
      }

      it("should only include active requests if pending unsupported") {
        val expected = Seq(
          BreakpointRequestInfo(TestRequestId, "some file", 999)
        )

        (mockBreakpointManager.breakpointRequestList _).expects()
          .returning(expected).once()

        val actual = pureBreakpointProfile.breakpointRequests

        actual should be (expected)
      }
    }

    describe("#onBreakpointWithData") {
      it("should create a new request if one has not be made yet") {
        val fileName = "some file"
        val lineNumber = 999
        val arguments = Seq(mock[JDIRequestArgument])

        val uniqueIdProperty = UniqueIdProperty(id = TestRequestId)
        val uniqueIdPropertyFilter = UniqueIdPropertyFilter(id = TestRequestId)

        // Set a known test id so we can validate the unique property is added
        import scala.language.reflectiveCalls
        pureBreakpointProfile.setRequestId(TestRequestId)

        inSequence {
          // Memoized request function first checks to make sure the cache
          // has not been invalidated underneath (first call will always be
          // false since we have never created the request)
          (mockBreakpointManager.hasBreakpointRequest _)
            .expects(fileName, lineNumber)
            .returning(false).once()

          // NOTE: Expect the request to be created with a unique id
          (mockBreakpointManager.createBreakpointRequestWithId _).expects(
            TestRequestId,
            fileName,
            lineNumber,
            uniqueIdProperty +: arguments
          ).returning(Success("")).once()

          (mockEventManager.addEventDataStream _)
            .expects(BreakpointEventType, Seq(uniqueIdPropertyFilter))
            .returning(Pipeline.newPipeline(
              classOf[(Event, Seq[JDIEventDataResult])]
            )).once()
        }

        pureBreakpointProfile.onBreakpointWithData(
          fileName,
          lineNumber,
          arguments: _*
        )
      }

      it("should capture exceptions thrown when creating the request") {
        val expected = Failure(new Throwable)
        val fileName = "some file"
        val lineNumber = 999
        val arguments = Seq(mock[JDIRequestArgument])

        val uniqueIdProperty = UniqueIdProperty(id = TestRequestId)

        // Set a known test id so we can validate the unique property is added
        import scala.language.reflectiveCalls
        pureBreakpointProfile.setRequestId(TestRequestId)

        inSequence {
          // Memoized request function first checks to make sure the cache
          // has not been invalidated underneath (first call will always be
          // false since we have never created the request)
          (mockBreakpointManager.hasBreakpointRequest _)
            .expects(fileName, lineNumber)
            .returning(false).once()

          // NOTE: Expect the request to be created with a unique id
          (mockBreakpointManager.createBreakpointRequestWithId _).expects(
            TestRequestId,
            fileName,
            lineNumber,
            uniqueIdProperty +: arguments
          ).throwing(expected.failed.get).once()
        }

        val actual = pureBreakpointProfile.onBreakpointWithData(
          fileName,
          lineNumber,
          arguments: _*
        )

        actual should be (expected)
      }

      it("should create a new request if the previous one was removed") {
        val fileName = "some file"
        val lineNumber = 999
        val arguments = Seq(mock[JDIRequestArgument])

        // Set a known test id so we can validate the unique property is added
        import scala.language.reflectiveCalls
        pureBreakpointProfile.setRequestId(TestRequestId)

        inSequence {
          // Set a known test id so we can validate the unique property is added
          import scala.language.reflectiveCalls
          pureBreakpointProfile.setRequestId(TestRequestId)

          val uniqueIdProperty = UniqueIdProperty(id = TestRequestId)
          val uniqueIdPropertyFilter =
            UniqueIdPropertyFilter(id = TestRequestId)

          // Memoized request function first checks to make sure the cache
          // has not been invalidated underneath (first call will always be
          // false since we have never created the request)
          (mockBreakpointManager.hasBreakpointRequest _)
            .expects(fileName, lineNumber)
            .returning(false).once()

          // NOTE: Expect the request to be created with a unique id
          (mockBreakpointManager.createBreakpointRequestWithId _).expects(
            TestRequestId,
            fileName,
            lineNumber,
            uniqueIdProperty +: arguments
          ).returning(Success("")).once()

          (mockEventManager.addEventDataStream _)
            .expects(BreakpointEventType, Seq(uniqueIdPropertyFilter))
            .returning(Pipeline.newPipeline(
              classOf[(Event, Seq[JDIEventDataResult])]
            )).once()
        }

        pureBreakpointProfile.onBreakpointWithData(
          fileName,
          lineNumber,
          arguments: _*
        )

        inSequence {
          // Set a known test id so we can validate the unique property is added
          import scala.language.reflectiveCalls
          pureBreakpointProfile.setRequestId(TestRequestId + "other")

          val uniqueIdProperty = UniqueIdProperty(id = TestRequestId + "other")
          val uniqueIdPropertyFilter =
            UniqueIdPropertyFilter(id = TestRequestId + "other")

          // Return false this time to indicate that the breakpoint request
          // was removed some time between the two calls
          (mockBreakpointManager.hasBreakpointRequest _)
            .expects(fileName, lineNumber)
            .returning(false).once()

          // NOTE: Expect the request to be created with a unique id
          (mockBreakpointManager.createBreakpointRequestWithId _).expects(
            TestRequestId + "other",
            fileName,
            lineNumber,
            uniqueIdProperty +: arguments
          ).returning(Success("")).once()

          (mockEventManager.addEventDataStream _)
            .expects(BreakpointEventType, Seq(uniqueIdPropertyFilter))
            .returning(Pipeline.newPipeline(
              classOf[(Event, Seq[JDIEventDataResult])]
            )).once()
        }

        pureBreakpointProfile.onBreakpointWithData(
          fileName,
          lineNumber,
          arguments: _*
        )
      }

      it("should not create a new request if the previous one still exists") {
        val fileName = "some file"
        val lineNumber = 999
        val arguments = Seq(mock[JDIRequestArgument])

        inSequence {
          // Set a known test id so we can validate the unique property is added
          import scala.language.reflectiveCalls
          pureBreakpointProfile.setRequestId(TestRequestId)

          val uniqueIdProperty = UniqueIdProperty(id = TestRequestId)
          val uniqueIdPropertyFilter =
            UniqueIdPropertyFilter(id = TestRequestId)

          // Memoized request function first checks to make sure the cache
          // has not been invalidated underneath (first call will always be
          // false since we have never created the request)
          (mockBreakpointManager.hasBreakpointRequest _)
            .expects(fileName, lineNumber)
            .returning(false).once()

          // NOTE: Expect the request to be created with a unique id
          (mockBreakpointManager.createBreakpointRequestWithId _).expects(
            TestRequestId,
            fileName,
            lineNumber,
            uniqueIdProperty +: arguments
          ).returning(Success("")).once()

          (mockEventManager.addEventDataStream _)
            .expects(BreakpointEventType, Seq(uniqueIdPropertyFilter))
            .returning(Pipeline.newPipeline(
              classOf[(Event, Seq[JDIEventDataResult])]
            )).once()
        }

        pureBreakpointProfile.onBreakpointWithData(
          fileName,
          lineNumber,
          arguments: _*
        )

        inSequence {
          // Set a known test id so we can validate the unique property is added
          import scala.language.reflectiveCalls
          pureBreakpointProfile.setRequestId(TestRequestId + "other")

          val uniqueIdProperty = UniqueIdProperty(id = TestRequestId)
          val uniqueIdPropertyFilter =
            UniqueIdPropertyFilter(id = TestRequestId)

          // Return true to indicate that we do still have the request
          (mockBreakpointManager.hasBreakpointRequest _)
            .expects(fileName, lineNumber)
            .returning(true).once()

          (mockEventManager.addEventDataStream _)
            .expects(BreakpointEventType, Seq(uniqueIdPropertyFilter))
            .returning(Pipeline.newPipeline(
              classOf[(Event, Seq[JDIEventDataResult])]
            )).once()
        }

        pureBreakpointProfile.onBreakpointWithData(
          fileName,
          lineNumber,
          arguments: _*
        )
      }

      it("should create a new request for different input") {
        val fileName = "some file"
        val lineNumber = 999
        val arguments = Seq(mock[JDIRequestArgument])

        // Set a known test id so we can validate the unique property is added
        import scala.language.reflectiveCalls
        pureBreakpointProfile.setRequestId(TestRequestId)

        inSequence {
          // Set a known test id so we can validate the unique property is added
          import scala.language.reflectiveCalls
          pureBreakpointProfile.setRequestId(TestRequestId)

          val uniqueIdProperty = UniqueIdProperty(id = TestRequestId)
          val uniqueIdPropertyFilter =
            UniqueIdPropertyFilter(id = TestRequestId)

          // Memoized request function first checks to make sure the cache
          // has not been invalidated underneath (first call will always be
          // false since we have never created the request)
          (mockBreakpointManager.hasBreakpointRequest _)
            .expects(fileName, lineNumber)
            .returning(false).once()

          // NOTE: Expect the request to be created with a unique id
          (mockBreakpointManager.createBreakpointRequestWithId _).expects(
            TestRequestId,
            fileName,
            lineNumber,
            uniqueIdProperty +: arguments
          ).returning(Success("")).once()

          (mockEventManager.addEventDataStream _)
            .expects(BreakpointEventType, Seq(uniqueIdPropertyFilter))
            .returning(Pipeline.newPipeline(
              classOf[(Event, Seq[JDIEventDataResult])]
            )).once()
        }

        pureBreakpointProfile.onBreakpointWithData(
          fileName,
          lineNumber,
          arguments: _*
        )

        inSequence {
          // Set a known test id so we can validate the unique property is added
          import scala.language.reflectiveCalls
          pureBreakpointProfile.setRequestId(TestRequestId + "other")

          val uniqueIdProperty = UniqueIdProperty(id = TestRequestId + "other")
          val uniqueIdPropertyFilter =
            UniqueIdPropertyFilter(id = TestRequestId + "other")

          // Memoized request function first checks to make sure the cache
          // has not been invalidated underneath (first call will always be
          // false since we have never created the request)
          (mockBreakpointManager.hasBreakpointRequest _)
            .expects(fileName, lineNumber + 1)
            .returning(false).once()

          // NOTE: Expect the request to be created with a unique id
          (mockBreakpointManager.createBreakpointRequestWithId _).expects(
            TestRequestId + "other",
            fileName,
            lineNumber + 1,
            uniqueIdProperty +: arguments
          ).returning(Success("")).once()

          (mockEventManager.addEventDataStream _)
            .expects(BreakpointEventType, Seq(uniqueIdPropertyFilter))
            .returning(Pipeline.newPipeline(
              classOf[(Event, Seq[JDIEventDataResult])]
            )).once()
        }

        pureBreakpointProfile.onBreakpointWithData(
          fileName,
          lineNumber + 1,
          arguments: _*
        )
      }

      it("should remove the underlying request if all pipelines are closed") {
        val fileName = "some file"
        val lineNumber = 999
        val arguments = Seq(mock[JDIRequestArgument])

        // Set a known test id so we can validate the unique property is added
        import scala.language.reflectiveCalls
        pureBreakpointProfile.setRequestId(TestRequestId)

        inSequence {
          val eventHandlerIds = Seq("a", "b")
          inAnyOrder {
            val uniqueIdProperty = UniqueIdProperty(id = TestRequestId)
            val uniqueIdPropertyFilter =
              UniqueIdPropertyFilter(id = TestRequestId)

            // Memoized request function first checks to make sure the cache
            // has not been invalidated underneath (first call will always be
            // empty since we have never created the request)
            (mockBreakpointManager.hasBreakpointRequest _)
              .expects(fileName, lineNumber)
              .returning(false).once()
            (mockBreakpointManager.hasBreakpointRequest _)
              .expects(fileName, lineNumber)
              .returning(true).once()

            // NOTE: Expect the request to be created with a unique id
            (mockBreakpointManager.createBreakpointRequestWithId _).expects(
              TestRequestId,
              fileName,
              lineNumber,
              uniqueIdProperty +: arguments
            ).returning(Success("")).once()

            // NOTE: Pipeline adds an event handler id to its metadata
            def newEventPipeline(id: String) = Pipeline.newPipeline(
              classOf[(Event, Seq[JDIEventDataResult])]
            ).withMetadata(Map(EventManager.EventHandlerIdMetadataField -> id))

            eventHandlerIds.foreach(id => {
              (mockEventManager.addEventDataStream _)
                .expects(BreakpointEventType, Seq(uniqueIdPropertyFilter))
                .returning(newEventPipeline(id)).once()
            })
          }

          (mockBreakpointManager.removeBreakpointRequestWithId _)
            .expects(TestRequestId).once()
          eventHandlerIds.foreach(id => {
            (mockEventManager.removeEventHandler _).expects(id).once()
          })
        }

        val p1 = pureBreakpointProfile.onBreakpointWithData(
          fileName,
          lineNumber,
          arguments: _*
        )
        val p2 = pureBreakpointProfile.onBreakpointWithData(
          fileName,
          lineNumber,
          arguments: _*
        )

        p1.foreach(_.close())
        p2.foreach(_.close())
      }

      it("should remove the underlying request if close data says to do so") {
        val fileName = "some file"
        val lineNumber = 999
        val arguments = Seq(mock[JDIRequestArgument])

        // Set a known test id so we can validate the unique property is added
        import scala.language.reflectiveCalls
        pureBreakpointProfile.setRequestId(TestRequestId)

        inSequence {
          val eventHandlerIds = Seq("a", "b")
          inAnyOrder {
            val uniqueIdProperty = UniqueIdProperty(id = TestRequestId)
            val uniqueIdPropertyFilter =
              UniqueIdPropertyFilter(id = TestRequestId)

            // Memoized request function first checks to make sure the cache
            // has not been invalidated underneath (first call will always be
            // empty since we have never created the request)
            (mockBreakpointManager.hasBreakpointRequest _)
              .expects(fileName, lineNumber)
              .returning(false).once()
            (mockBreakpointManager.hasBreakpointRequest _)
              .expects(fileName, lineNumber)
              .returning(true).once()

            // NOTE: Expect the request to be created with a unique id
            (mockBreakpointManager.createBreakpointRequestWithId _).expects(
              TestRequestId,
              fileName,
              lineNumber,
              uniqueIdProperty +: arguments
            ).returning(Success("")).once()

            // NOTE: Pipeline adds an event handler id to its metadata
            def newEventPipeline(id: String) = Pipeline.newPipeline(
              classOf[(Event, Seq[JDIEventDataResult])]
            ).withMetadata(Map(EventManager.EventHandlerIdMetadataField -> id))

            eventHandlerIds.foreach(id => {
              (mockEventManager.addEventDataStream _)
                .expects(BreakpointEventType, Seq(uniqueIdPropertyFilter))
                .returning(newEventPipeline(id)).once()
            })
          }

          (mockBreakpointManager.removeBreakpointRequestWithId _)
            .expects(TestRequestId).once()
          eventHandlerIds.foreach(id => {
            (mockEventManager.removeEventHandler _).expects(id).once()
          })
        }

        val p1 = pureBreakpointProfile.onBreakpointWithData(
          fileName,
          lineNumber,
          arguments: _*
        )
        val p2 = pureBreakpointProfile.onBreakpointWithData(
          fileName,
          lineNumber,
          arguments: _*
        )

        p1.foreach(_.close(now = true, data = Constants.CloseRemoveAll))
      }
    }
  }
}
