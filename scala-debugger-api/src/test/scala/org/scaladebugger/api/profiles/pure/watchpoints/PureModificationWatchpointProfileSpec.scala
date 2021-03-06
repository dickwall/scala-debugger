package org.scaladebugger.api.profiles.pure.watchpoints
import acyclic.file

import com.sun.jdi.event.Event
import org.scaladebugger.api.lowlevel.classes.ClassManager
import org.scaladebugger.api.lowlevel.events.EventManager
import org.scaladebugger.api.lowlevel.events.EventType.ModificationWatchpointEventType
import org.scaladebugger.api.lowlevel.events.data.JDIEventDataResult
import org.scaladebugger.api.lowlevel.events.filters.UniqueIdPropertyFilter
import org.scaladebugger.api.lowlevel.requests.JDIRequestArgument
import org.scaladebugger.api.lowlevel.requests.properties.UniqueIdProperty
import org.scaladebugger.api.lowlevel.watchpoints.{ModificationWatchpointManager, ModificationWatchpointRequestInfo, PendingModificationWatchpointSupportLike}
import org.scaladebugger.api.pipelines.Pipeline
import org.scaladebugger.api.profiles.Constants
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSpec, Matchers, ParallelTestExecution}
import test.JDIMockHelpers

import scala.util.{Failure, Success}

class PureModificationWatchpointProfileSpec extends FunSpec with Matchers
  with ParallelTestExecution with MockFactory with JDIMockHelpers
{
  private val TestRequestId = java.util.UUID.randomUUID().toString
  private val stubClassManager = stub[ClassManager]
  private val mockModificationWatchpointManager = mock[ModificationWatchpointManager]
  private val mockEventManager = mock[EventManager]

  private val pureModificationWatchpointProfile = new Object with PureModificationWatchpointProfile {
    private var requestId: String = _
    def setRequestId(requestId: String): Unit = this.requestId = requestId

    // NOTE: If we set a specific request id, return that, otherwise use the
    //       default behavior
    override protected def newModificationWatchpointRequestId(): String =
      if (requestId != null) requestId else super.newModificationWatchpointRequestId()

    override protected val modificationWatchpointManager = mockModificationWatchpointManager
    override protected val eventManager: EventManager = mockEventManager
  }

  describe("PureModificationWatchpointProfile") {
    describe("#modificationWatchpointRequests") {
      it("should include all active requests") {
        val expected = Seq(
          ModificationWatchpointRequestInfo(
            TestRequestId,
            false,
            "some.class.name",
            "someFieldName"
          )
        )

        val mockModificationWatchpointManager = mock[PendingModificationWatchpointSupportLike]
        val pureModificationWatchpointProfile = new Object with PureModificationWatchpointProfile {
          override protected val modificationWatchpointManager = mockModificationWatchpointManager
          override protected val eventManager: EventManager = mockEventManager
        }

        (mockModificationWatchpointManager.modificationWatchpointRequestList _).expects()
          .returning(expected).once()

        (mockModificationWatchpointManager.pendingModificationWatchpointRequests _).expects()
          .returning(Nil).once()

        val actual = pureModificationWatchpointProfile.modificationWatchpointRequests

        actual should be (expected)
      }

      it("should include pending requests if supported") {
        val expected = Seq(
          ModificationWatchpointRequestInfo(
            TestRequestId,
            true,
            "some.class.name",
            "someFieldName"
          )
        )

        val mockModificationWatchpointManager = mock[PendingModificationWatchpointSupportLike]
        val pureModificationWatchpointProfile = new Object with PureModificationWatchpointProfile {
          override protected val modificationWatchpointManager = mockModificationWatchpointManager
          override protected val eventManager: EventManager = mockEventManager
        }

        (mockModificationWatchpointManager.modificationWatchpointRequestList _).expects()
          .returning(Nil).once()

        (mockModificationWatchpointManager.pendingModificationWatchpointRequests _).expects()
          .returning(expected).once()

        val actual = pureModificationWatchpointProfile.modificationWatchpointRequests

        actual should be (expected)
      }

      it("should only include active requests if pending unsupported") {
        val expected = Seq(
          ModificationWatchpointRequestInfo(
            TestRequestId,
            false,
            "some.class.name",
            "someFieldName"
          )
        )

        (mockModificationWatchpointManager.modificationWatchpointRequestList _).expects()
          .returning(expected).once()

        val actual = pureModificationWatchpointProfile.modificationWatchpointRequests

        actual should be (expected)
      }
    }

    describe("#removeModificationWatchpointRequests") {
      it("should return empty if no requests exists") {
        val expected = Nil
        val className = "some.class.name"
        val fieldName = "someFieldName"

        (mockModificationWatchpointManager.modificationWatchpointRequestList _).expects()
          .returning(Nil).once()

        val actual = pureModificationWatchpointProfile.removeModificationWatchpointRequests(
          className,
          fieldName
        )

        actual should be (expected)
      }

      it("should return empty if no request with matching filename exists") {
        val expected = Nil
        val className = "some.class.name"
        val fieldName = "someFieldName"
        val extraArguments = Seq(mock[JDIRequestArgument])

        val requests = Seq(
          ModificationWatchpointRequestInfo(
            requestId = TestRequestId,
            isPending = true,
            className = className + "other",
            fieldName = fieldName,
            extraArguments = extraArguments
          )
        )

        (mockModificationWatchpointManager.modificationWatchpointRequestList _).expects()
          .returning(requests).once()

        val actual = pureModificationWatchpointProfile.removeModificationWatchpointRequests(
          className,
          fieldName
        )

        actual should be (expected)
      }

      it("should return empty if no request with matching line number exists") {
        val expected = Nil
        val className = "some.class.name"
        val fieldName = "someFieldName"
        val extraArguments = Seq(mock[JDIRequestArgument])

        val requests = Seq(
          ModificationWatchpointRequestInfo(
            requestId = TestRequestId,
            isPending = true,
            className = className,
            fieldName = fieldName + 1,
            extraArguments = extraArguments
          )
        )

        (mockModificationWatchpointManager.modificationWatchpointRequestList _).expects()
          .returning(requests).once()

        val actual = pureModificationWatchpointProfile.removeModificationWatchpointRequests(
          className,
          fieldName
        )

        actual should be (expected)
      }

      it("should return remove and return matching pending requests") {
        val className = "some.class.name"
        val fieldName = "someFieldName"
        val extraArguments = Seq(mock[JDIRequestArgument])

        val expected = Seq(
          ModificationWatchpointRequestInfo(
            requestId = TestRequestId,
            isPending = true,
            className = className,
            fieldName = fieldName,
            extraArguments = extraArguments
          )
        )

        (mockModificationWatchpointManager.modificationWatchpointRequestList _).expects()
          .returning(expected).once()
        expected.foreach(b =>
          (mockModificationWatchpointManager.removeModificationWatchpointRequestWithId _)
            .expects(b.requestId)
            .returning(true)
            .once()
        )

        val actual = pureModificationWatchpointProfile.removeModificationWatchpointRequests(
          className,
          fieldName
        )

        actual should be (expected)
      }

      it("should remove and return matching non-pending requests") {
        val className = "some.class.name"
        val fieldName = "someFieldName"
        val extraArguments = Seq(mock[JDIRequestArgument])

        val expected = Seq(
          ModificationWatchpointRequestInfo(
            requestId = TestRequestId,
            isPending = false,
            className = className,
            fieldName = fieldName,
            extraArguments = extraArguments
          )
        )

        (mockModificationWatchpointManager.modificationWatchpointRequestList _).expects()
          .returning(expected).once()
        expected.foreach(b =>
          (mockModificationWatchpointManager.removeModificationWatchpointRequestWithId _)
            .expects(b.requestId)
            .returning(true)
            .once()
        )

        val actual = pureModificationWatchpointProfile.removeModificationWatchpointRequests(
          className,
          fieldName
        )

        actual should be (expected)
      }
    }

    describe("#removeModificationWatchpointRequestWithArgs") {
      it("should return None if no requests exists") {
        val expected = None
        val className = "some.class.name"
        val fieldName = "someFieldName"

        (mockModificationWatchpointManager.modificationWatchpointRequestList _).expects()
          .returning(Nil).once()

        val actual = pureModificationWatchpointProfile.removeModificationWatchpointRequestWithArgs(
          className,
          fieldName
        )

        actual should be (expected)
      }

      it("should return None if no request with matching filename exists") {
        val expected = None
        val className = "some.class.name"
        val fieldName = "someFieldName"
        val extraArguments = Seq(mock[JDIRequestArgument])

        val requests = Seq(
          ModificationWatchpointRequestInfo(
            requestId = TestRequestId,
            isPending = true,
            className = className + "other",
            fieldName = fieldName,
            extraArguments = extraArguments
          )
        )

        (mockModificationWatchpointManager.modificationWatchpointRequestList _).expects()
          .returning(requests).once()

        val actual = pureModificationWatchpointProfile.removeModificationWatchpointRequestWithArgs(
          className,
          fieldName,
          extraArguments: _*
        )

        actual should be (expected)
      }

      it("should return None if no request with matching line number exists") {
        val expected = None
        val className = "some.class.name"
        val fieldName = "someFieldName"
        val extraArguments = Seq(mock[JDIRequestArgument])

        val requests = Seq(
          ModificationWatchpointRequestInfo(
            requestId = TestRequestId,
            isPending = true,
            className = className,
            fieldName = fieldName + 1,
            extraArguments = extraArguments
          )
        )

        (mockModificationWatchpointManager.modificationWatchpointRequestList _).expects()
          .returning(requests).once()

        val actual = pureModificationWatchpointProfile.removeModificationWatchpointRequestWithArgs(
          className,
          fieldName,
          extraArguments: _*
        )

        actual should be (expected)
      }

      it("should return None if no request with matching extra arguments exists") {
        val expected = None
        val className = "some.class.name"
        val fieldName = "someFieldName"
        val extraArguments = Seq(mock[JDIRequestArgument])

        val requests = Seq(
          ModificationWatchpointRequestInfo(
            requestId = TestRequestId,
            isPending = true,
            className = className,
            fieldName = fieldName,
            extraArguments = extraArguments
          )
        )

        (mockModificationWatchpointManager.modificationWatchpointRequestList _).expects()
          .returning(requests).once()

        val actual = pureModificationWatchpointProfile.removeModificationWatchpointRequestWithArgs(
          className,
          fieldName
        )

        actual should be (expected)
      }

      it("should return remove and return matching pending requests") {
        val className = "some.class.name"
        val fieldName = "someFieldName"
        val extraArguments = Seq(mock[JDIRequestArgument])

        val expected = Some(
          ModificationWatchpointRequestInfo(
            requestId = TestRequestId,
            isPending = true,
            className = className,
            fieldName = fieldName,
            extraArguments = extraArguments
          )
        )

        (mockModificationWatchpointManager.modificationWatchpointRequestList _).expects()
          .returning(Seq(expected.get)).once()
        expected.foreach(b =>
          (mockModificationWatchpointManager.removeModificationWatchpointRequestWithId _)
            .expects(b.requestId)
            .returning(true)
            .once()
        )

        val actual = pureModificationWatchpointProfile.removeModificationWatchpointRequestWithArgs(
          className,
          fieldName,
          extraArguments: _*
        )

        actual should be (expected)
      }

      it("should remove and return matching non-pending requests") {
        val className = "some.class.name"
        val fieldName = "someFieldName"
        val extraArguments = Seq(mock[JDIRequestArgument])

        val expected = Some(
          ModificationWatchpointRequestInfo(
            requestId = TestRequestId,
            isPending = false,
            className = className,
            fieldName = fieldName,
            extraArguments = extraArguments
          )
        )

        (mockModificationWatchpointManager.modificationWatchpointRequestList _).expects()
          .returning(Seq(expected.get)).once()
        expected.foreach(b =>
          (mockModificationWatchpointManager.removeModificationWatchpointRequestWithId _)
            .expects(b.requestId)
            .returning(true)
            .once()
        )

        val actual = pureModificationWatchpointProfile.removeModificationWatchpointRequestWithArgs(
          className,
          fieldName,
          extraArguments: _*
        )

        actual should be (expected)
      }
    }

    describe("#removeAllModificationWatchpointRequests") {
      it("should return empty if no requests exists") {
        val expected = Nil
        val className = "some.class.name"
        val fieldName = "someFieldName"

        (mockModificationWatchpointManager.modificationWatchpointRequestList _).expects()
          .returning(Nil).once()

        val actual = pureModificationWatchpointProfile.removeAllModificationWatchpointRequests()

        actual should be (expected)
      }

      it("should remove and return all pending requests") {
        val className = "some.class.name"
        val fieldName = "someFieldName"
        val extraArguments = Seq(mock[JDIRequestArgument])

        val expected = Seq(
          ModificationWatchpointRequestInfo(
            requestId = TestRequestId,
            isPending = true,
            className = className,
            fieldName = fieldName,
            extraArguments = extraArguments
          )
        )

        (mockModificationWatchpointManager.modificationWatchpointRequestList _).expects()
          .returning(expected).once()
        expected.foreach(b =>
          (mockModificationWatchpointManager.removeModificationWatchpointRequestWithId _)
            .expects(b.requestId)
            .returning(true)
            .once()
        )

        val actual = pureModificationWatchpointProfile.removeAllModificationWatchpointRequests()

        actual should be (expected)
      }

      it("should remove and return all non-pending requests") {
        val className = "some.class.name"
        val fieldName = "someFieldName"
        val extraArguments = Seq(mock[JDIRequestArgument])

        val expected = Seq(
          ModificationWatchpointRequestInfo(
            requestId = TestRequestId,
            isPending = false,
            className = className,
            fieldName = fieldName,
            extraArguments = extraArguments
          )
        )

        (mockModificationWatchpointManager.modificationWatchpointRequestList _).expects()
          .returning(expected).once()
        expected.foreach(b =>
          (mockModificationWatchpointManager.removeModificationWatchpointRequestWithId _)
            .expects(b.requestId)
            .returning(true)
            .once()
        )

        val actual = pureModificationWatchpointProfile.removeAllModificationWatchpointRequests()

        actual should be (expected)
      }
    }

    describe("#isModificationWatchpointRequestPending") {
      it("should return false if no requests exist") {
        val expected = false
        val className = "some.class.name"
        val fieldName = "someFieldName"

        (mockModificationWatchpointManager.modificationWatchpointRequestList _).expects()
          .returning(Nil).once()

        val actual = pureModificationWatchpointProfile.isModificationWatchpointRequestPending(
          className,
          fieldName
        )

        actual should be (expected)
      }

      it("should return false if no request with matching class name exists") {
        val expected = false
        val className = "some.class.name"
        val fieldName = "someFieldName"
        val extraArguments = Seq(mock[JDIRequestArgument])

        val requests = Seq(
          ModificationWatchpointRequestInfo(
            requestId = TestRequestId,
            isPending = true,
            className = className + "other",
            fieldName = fieldName,
            extraArguments = extraArguments
          )
        )

        (mockModificationWatchpointManager.modificationWatchpointRequestList _).expects()
          .returning(requests).once()

        val actual = pureModificationWatchpointProfile.isModificationWatchpointRequestPending(
          className,
          fieldName
        )

        actual should be (expected)
      }

      it("should return false if no request with matching field name exists") {
        val expected = false
        val className = "some.class.name"
        val fieldName = "someFieldName"
        val extraArguments = Seq(mock[JDIRequestArgument])

        val requests = Seq(
          ModificationWatchpointRequestInfo(
            requestId = TestRequestId,
            isPending = true,
            className = className,
            fieldName = fieldName + 1,
            extraArguments = extraArguments
          )
        )

        (mockModificationWatchpointManager.modificationWatchpointRequestList _).expects()
          .returning(requests).once()

        val actual = pureModificationWatchpointProfile.isModificationWatchpointRequestPending(
          className,
          fieldName
        )

        actual should be (expected)
      }

      it("should return false if no matching request is pending") {
        val expected = false
        val className = "some.class.name"
        val fieldName = "someFieldName"
        val extraArguments = Seq(mock[JDIRequestArgument])

        val requests = Seq(
          ModificationWatchpointRequestInfo(
            requestId = TestRequestId,
            isPending = false,
            className = className,
            fieldName = fieldName,
            extraArguments = extraArguments
          )
        )

        (mockModificationWatchpointManager.modificationWatchpointRequestList _).expects()
          .returning(requests).once()

        val actual = pureModificationWatchpointProfile.isModificationWatchpointRequestPending(
          className,
          fieldName
        )

        actual should be (expected)
      }

      it("should return true if at least one matching request is pending") {
        val expected = true
        val className = "some.class.name"
        val fieldName = "someFieldName"
        val extraArguments = Seq(mock[JDIRequestArgument])

        val requests = Seq(
          ModificationWatchpointRequestInfo(
            requestId = TestRequestId,
            isPending = true,
            className = className,
            fieldName = fieldName,
            extraArguments = extraArguments
          )
        )

        (mockModificationWatchpointManager.modificationWatchpointRequestList _).expects()
          .returning(requests).once()

        val actual = pureModificationWatchpointProfile.isModificationWatchpointRequestPending(
          className,
          fieldName
        )

        actual should be (expected)
      }
    }

    describe("#isModificationWatchpointRequestWithArgsPending") {
      it("should return false if no requests exist") {
        val expected = false
        val className = "some.class.name"
        val fieldName = "someFieldName"

        (mockModificationWatchpointManager.modificationWatchpointRequestList _).expects()
          .returning(Nil).once()

        val actual = pureModificationWatchpointProfile.isModificationWatchpointRequestWithArgsPending(
          className,
          fieldName
        )

        actual should be (expected)
      }

      it("should return false if no request with matching class name exists") {
        val expected = false
        val className = "some.class.name"
        val fieldName = "someFieldName"
        val extraArguments = Seq(mock[JDIRequestArgument])

        val requests = Seq(
          ModificationWatchpointRequestInfo(
            requestId = TestRequestId,
            isPending = true,
            className = className + "other",
            fieldName = fieldName,
            extraArguments = extraArguments
          )
        )

        (mockModificationWatchpointManager.modificationWatchpointRequestList _).expects()
          .returning(requests).once()

        val actual = pureModificationWatchpointProfile.isModificationWatchpointRequestWithArgsPending(
          className,
          fieldName
        )

        actual should be (expected)
      }

      it("should return false if no request with matching field name exists") {
        val expected = false
        val className = "some.class.name"
        val fieldName = "someFieldName"
        val extraArguments = Seq(mock[JDIRequestArgument])

        val requests = Seq(
          ModificationWatchpointRequestInfo(
            requestId = TestRequestId,
            isPending = true,
            className = className,
            fieldName = fieldName + 1,
            extraArguments = extraArguments
          )
        )

        (mockModificationWatchpointManager.modificationWatchpointRequestList _).expects()
          .returning(requests).once()

        val actual = pureModificationWatchpointProfile.isModificationWatchpointRequestWithArgsPending(
          className,
          fieldName
        )

        actual should be (expected)
      }

      it("should return false if no request with matching extra arguments exists") {
        val expected = false
        val className = "some.class.name"
        val fieldName = "someFieldName"
        val extraArguments = Seq(mock[JDIRequestArgument])

        val requests = Seq(
          ModificationWatchpointRequestInfo(
            requestId = TestRequestId,
            isPending = true,
            className = className,
            fieldName = fieldName,
            extraArguments = extraArguments
          )
        )

        (mockModificationWatchpointManager.modificationWatchpointRequestList _).expects()
          .returning(requests).once()

        val actual = pureModificationWatchpointProfile.isModificationWatchpointRequestWithArgsPending(
          className,
          fieldName
        )

        actual should be (expected)
      }

      it("should return false if no matching request is pending") {
        val expected = false
        val className = "some.class.name"
        val fieldName = "someFieldName"
        val extraArguments = Seq(mock[JDIRequestArgument])

        val requests = Seq(
          ModificationWatchpointRequestInfo(
            requestId = TestRequestId,
            isPending = false,
            className = className,
            fieldName = fieldName,
            extraArguments = extraArguments
          )
        )

        (mockModificationWatchpointManager.modificationWatchpointRequestList _).expects()
          .returning(requests).once()

        val actual = pureModificationWatchpointProfile.isModificationWatchpointRequestWithArgsPending(
          className,
          fieldName,
          extraArguments: _*
        )

        actual should be (expected)
      }

      it("should return true if at least one matching request is pending") {
        val expected = true
        val className = "some.class.name"
        val fieldName = "someFieldName"
        val extraArguments = Seq(mock[JDIRequestArgument])

        val requests = Seq(
          ModificationWatchpointRequestInfo(
            requestId = TestRequestId,
            isPending = true,
            className = className,
            fieldName = fieldName,
            extraArguments = extraArguments
          )
        )

        (mockModificationWatchpointManager.modificationWatchpointRequestList _).expects()
          .returning(requests).once()

        val actual = pureModificationWatchpointProfile.isModificationWatchpointRequestWithArgsPending(
          className,
          fieldName,
          extraArguments: _*
        )

        actual should be (expected)
      }
    }
    describe("#tryGetOrCreateModificationWatchpointRequestWithData") {
      it("should create a new request if one has not be made yet") {
        val className = "full.class.name"
        val fieldName = "fieldName"
        val arguments = Seq(mock[JDIRequestArgument])

        val uniqueIdProperty = UniqueIdProperty(id = TestRequestId)
        val uniqueIdPropertyFilter = UniqueIdPropertyFilter(id = TestRequestId)

        // Set a known test id so we can validate the unique property is added
        import scala.language.reflectiveCalls
        pureModificationWatchpointProfile.setRequestId(TestRequestId)

        inSequence {
          // Memoized request function first checks to make sure the cache
          // has not been invalidated underneath (first call will always be
          // false since we have never created the request)
          (mockModificationWatchpointManager.hasModificationWatchpointRequest _)
            .expects(className, fieldName)
            .returning(false).once()

          // NOTE: Expect the request to be created with a unique id
          (mockModificationWatchpointManager.createModificationWatchpointRequestWithId _).expects(
            TestRequestId,
            className,
            fieldName,
            uniqueIdProperty +: arguments
          ).returning(Success("")).once()

          (mockEventManager.addEventDataStream _)
            .expects(ModificationWatchpointEventType, Seq(uniqueIdPropertyFilter))
            .returning(Pipeline.newPipeline(
              classOf[(Event, Seq[JDIEventDataResult])]
            )).once()
        }

        pureModificationWatchpointProfile.tryGetOrCreateModificationWatchpointRequestWithData(
          className,
          fieldName,
          arguments: _*
        )
      }

      it("should capture exceptions thrown when creating the request") {
        val expected = Failure(new Throwable)
        val className = "full.class.name"
        val fieldName = "fieldName"
        val arguments = Seq(mock[JDIRequestArgument])

        val uniqueIdProperty = UniqueIdProperty(id = TestRequestId)

        // Set a known test id so we can validate the unique property is added
        import scala.language.reflectiveCalls
        pureModificationWatchpointProfile.setRequestId(TestRequestId)

        inSequence {
          // Memoized request function first checks to make sure the cache
          // has not been invalidated underneath (first call will always be
          // false since we have never created the request)
          (mockModificationWatchpointManager.hasModificationWatchpointRequest _)
            .expects(className, fieldName)
            .returning(false).once()

          // NOTE: Expect the request to be created with a unique id
          (mockModificationWatchpointManager.createModificationWatchpointRequestWithId _).expects(
            TestRequestId,
            className,
            fieldName,
            uniqueIdProperty +: arguments
          ).throwing(expected.failed.get).once()
        }

        val actual = pureModificationWatchpointProfile.tryGetOrCreateModificationWatchpointRequestWithData(
          className,
          fieldName,
          arguments: _*
        )

        actual should be (expected)
      }

      it("should create a new request if the previous one was removed") {
        val className = "full.class.name"
        val fieldName = "fieldName"
        val arguments = Seq(mock[JDIRequestArgument])

        // Set a known test id so we can validate the unique property is added
        import scala.language.reflectiveCalls
        pureModificationWatchpointProfile.setRequestId(TestRequestId)

        inSequence {
          // Set a known test id so we can validate the unique property is added
          import scala.language.reflectiveCalls
          pureModificationWatchpointProfile.setRequestId(TestRequestId)

          val uniqueIdProperty = UniqueIdProperty(id = TestRequestId)
          val uniqueIdPropertyFilter =
            UniqueIdPropertyFilter(id = TestRequestId)

          // Memoized request function first checks to make sure the cache
          // has not been invalidated underneath (first call will always be
          // false since we have never created the request)
          (mockModificationWatchpointManager.hasModificationWatchpointRequest _)
            .expects(className, fieldName)
            .returning(false).once()

          // NOTE: Expect the request to be created with a unique id
          (mockModificationWatchpointManager.createModificationWatchpointRequestWithId _).expects(
            TestRequestId,
            className,
            fieldName,
            uniqueIdProperty +: arguments
          ).returning(Success("")).once()

          (mockEventManager.addEventDataStream _)
            .expects(ModificationWatchpointEventType, Seq(uniqueIdPropertyFilter))
            .returning(Pipeline.newPipeline(
              classOf[(Event, Seq[JDIEventDataResult])]
            )).once()
        }

        pureModificationWatchpointProfile.tryGetOrCreateModificationWatchpointRequestWithData(
          className,
          fieldName,
          arguments: _*
        )

        inSequence {
          // Set a known test id so we can validate the unique property is added
          import scala.language.reflectiveCalls
          pureModificationWatchpointProfile.setRequestId(TestRequestId + "other")

          val uniqueIdProperty = UniqueIdProperty(id = TestRequestId + "other")
          val uniqueIdPropertyFilter =
            UniqueIdPropertyFilter(id = TestRequestId + "other")

          // Return false this time to indicate that the modificationWatchpoint request
          // was removed some time between the two calls
          (mockModificationWatchpointManager.hasModificationWatchpointRequest _)
            .expects(className, fieldName)
            .returning(false).once()

          // NOTE: Expect the request to be created with a unique id
          (mockModificationWatchpointManager.createModificationWatchpointRequestWithId _).expects(
            TestRequestId + "other",
            className,
            fieldName,
            uniqueIdProperty +: arguments
          ).returning(Success("")).once()

          (mockEventManager.addEventDataStream _)
            .expects(ModificationWatchpointEventType, Seq(uniqueIdPropertyFilter))
            .returning(Pipeline.newPipeline(
              classOf[(Event, Seq[JDIEventDataResult])]
            )).once()
        }

        pureModificationWatchpointProfile.tryGetOrCreateModificationWatchpointRequestWithData(
          className,
          fieldName,
          arguments: _*
        )
      }

      it("should not create a new request if the previous one still exists") {
        val className = "full.class.name"
        val fieldName = "fieldName"
        val arguments = Seq(mock[JDIRequestArgument])

        inSequence {
          // Set a known test id so we can validate the unique property is added
          import scala.language.reflectiveCalls
          pureModificationWatchpointProfile.setRequestId(TestRequestId)

          val uniqueIdProperty = UniqueIdProperty(id = TestRequestId)
          val uniqueIdPropertyFilter =
            UniqueIdPropertyFilter(id = TestRequestId)

          // Memoized request function first checks to make sure the cache
          // has not been invalidated underneath (first call will always be
          // false since we have never created the request)
          (mockModificationWatchpointManager.hasModificationWatchpointRequest _)
            .expects(className, fieldName)
            .returning(false).once()

          // NOTE: Expect the request to be created with a unique id
          (mockModificationWatchpointManager.createModificationWatchpointRequestWithId _).expects(
            TestRequestId,
            className,
            fieldName,
            uniqueIdProperty +: arguments
          ).returning(Success("")).once()

          (mockEventManager.addEventDataStream _)
            .expects(ModificationWatchpointEventType, Seq(uniqueIdPropertyFilter))
            .returning(Pipeline.newPipeline(
              classOf[(Event, Seq[JDIEventDataResult])]
            )).once()
        }

        pureModificationWatchpointProfile.tryGetOrCreateModificationWatchpointRequestWithData(
          className,
          fieldName,
          arguments: _*
        )

        inSequence {
          // Set a known test id so we can validate the unique property is added
          import scala.language.reflectiveCalls
          pureModificationWatchpointProfile.setRequestId(TestRequestId + "other")

          val uniqueIdProperty = UniqueIdProperty(id = TestRequestId)
          val uniqueIdPropertyFilter =
            UniqueIdPropertyFilter(id = TestRequestId)

          // Return true to indicate that we do still have the request
          (mockModificationWatchpointManager.hasModificationWatchpointRequest _)
            .expects(className, fieldName)
            .returning(true).once()

          (mockEventManager.addEventDataStream _)
            .expects(ModificationWatchpointEventType, Seq(uniqueIdPropertyFilter))
            .returning(Pipeline.newPipeline(
              classOf[(Event, Seq[JDIEventDataResult])]
            )).once()
        }

        pureModificationWatchpointProfile.tryGetOrCreateModificationWatchpointRequestWithData(
          className,
          fieldName,
          arguments: _*
        )
      }

      it("should create a new request for different input") {
        val className = "full.class.name"
        val fieldName = "fieldName"
        val arguments = Seq(mock[JDIRequestArgument])

        // Set a known test id so we can validate the unique property is added
        import scala.language.reflectiveCalls
        pureModificationWatchpointProfile.setRequestId(TestRequestId)

        inSequence {
          // Set a known test id so we can validate the unique property is added
          import scala.language.reflectiveCalls
          pureModificationWatchpointProfile.setRequestId(TestRequestId)

          val uniqueIdProperty = UniqueIdProperty(id = TestRequestId)
          val uniqueIdPropertyFilter =
            UniqueIdPropertyFilter(id = TestRequestId)

          // Memoized request function first checks to make sure the cache
          // has not been invalidated underneath (first call will always be
          // false since we have never created the request)
          (mockModificationWatchpointManager.hasModificationWatchpointRequest _)
            .expects(className, fieldName)
            .returning(false).once()

          // NOTE: Expect the request to be created with a unique id
          (mockModificationWatchpointManager.createModificationWatchpointRequestWithId _).expects(
            TestRequestId,
            className,
            fieldName,
            uniqueIdProperty +: arguments
          ).returning(Success("")).once()

          (mockEventManager.addEventDataStream _)
            .expects(ModificationWatchpointEventType, Seq(uniqueIdPropertyFilter))
            .returning(Pipeline.newPipeline(
              classOf[(Event, Seq[JDIEventDataResult])]
            )).once()
        }

        pureModificationWatchpointProfile.tryGetOrCreateModificationWatchpointRequestWithData(
          className,
          fieldName,
          arguments: _*
        )

        inSequence {
          // Set a known test id so we can validate the unique property is added
          import scala.language.reflectiveCalls
          pureModificationWatchpointProfile.setRequestId(TestRequestId + "other")

          val uniqueIdProperty = UniqueIdProperty(id = TestRequestId + "other")
          val uniqueIdPropertyFilter =
            UniqueIdPropertyFilter(id = TestRequestId + "other")

          // Memoized request function first checks to make sure the cache
          // has not been invalidated underneath (first call will always be
          // false since we have never created the request)
          (mockModificationWatchpointManager.hasModificationWatchpointRequest _)
            .expects(className, fieldName + 1)
            .returning(false).once()

          // NOTE: Expect the request to be created with a unique id
          (mockModificationWatchpointManager.createModificationWatchpointRequestWithId _).expects(
            TestRequestId + "other",
            className,
            fieldName + 1,
            uniqueIdProperty +: arguments
          ).returning(Success("")).once()

          (mockEventManager.addEventDataStream _)
            .expects(ModificationWatchpointEventType, Seq(uniqueIdPropertyFilter))
            .returning(Pipeline.newPipeline(
              classOf[(Event, Seq[JDIEventDataResult])]
            )).once()
        }

        pureModificationWatchpointProfile.tryGetOrCreateModificationWatchpointRequestWithData(
          className,
          fieldName + 1,
          arguments: _*
        )
      }

      it("should remove the underlying request if all pipelines are closed") {
        val className = "full.class.name"
        val fieldName = "fieldName"
        val arguments = Seq(mock[JDIRequestArgument])

        // Set a known test id so we can validate the unique property is added
        import scala.language.reflectiveCalls
        pureModificationWatchpointProfile.setRequestId(TestRequestId)

        inSequence {
          val eventHandlerIds = Seq("a", "b")
          inAnyOrder {
            val uniqueIdProperty = UniqueIdProperty(id = TestRequestId)
            val uniqueIdPropertyFilter =
              UniqueIdPropertyFilter(id = TestRequestId)

            // Memoized request function first checks to make sure the cache
            // has not been invalidated underneath (first call will always be
            // empty since we have never created the request)
            (mockModificationWatchpointManager.hasModificationWatchpointRequest _)
              .expects(className, fieldName)
              .returning(false).once()
            (mockModificationWatchpointManager.hasModificationWatchpointRequest _)
              .expects(className, fieldName)
              .returning(true).once()

            // NOTE: Expect the request to be created with a unique id
            (mockModificationWatchpointManager.createModificationWatchpointRequestWithId _).expects(
              TestRequestId,
              className,
              fieldName,
              uniqueIdProperty +: arguments
            ).returning(Success("")).once()

            // NOTE: Pipeline adds an event handler id to its metadata
            def newEventPipeline(id: String) = Pipeline.newPipeline(
              classOf[(Event, Seq[JDIEventDataResult])]
            ).withMetadata(Map(EventManager.EventHandlerIdMetadataField -> id))

            eventHandlerIds.foreach(id => {
              (mockEventManager.addEventDataStream _)
                .expects(ModificationWatchpointEventType, Seq(uniqueIdPropertyFilter))
                .returning(newEventPipeline(id)).once()
            })
          }

          (mockModificationWatchpointManager.removeModificationWatchpointRequestWithId _)
            .expects(TestRequestId).once()
          eventHandlerIds.foreach(id => {
            (mockEventManager.removeEventHandler _).expects(id).once()
          })
        }

        val p1 = pureModificationWatchpointProfile.tryGetOrCreateModificationWatchpointRequestWithData(
          className,
          fieldName,
          arguments: _*
        )
        val p2 = pureModificationWatchpointProfile.tryGetOrCreateModificationWatchpointRequestWithData(
          className,
          fieldName,
          arguments: _*
        )

        p1.foreach(_.close())
        p2.foreach(_.close())
      }

      it("should remove the underlying request if close data says to do so") {
        val className = "full.class.name"
        val fieldName = "fieldName"
        val arguments = Seq(mock[JDIRequestArgument])

        // Set a known test id so we can validate the unique property is added
        import scala.language.reflectiveCalls
        pureModificationWatchpointProfile.setRequestId(TestRequestId)

        inSequence {
          val eventHandlerIds = Seq("a", "b")
          inAnyOrder {
            val uniqueIdProperty = UniqueIdProperty(id = TestRequestId)
            val uniqueIdPropertyFilter =
              UniqueIdPropertyFilter(id = TestRequestId)

            // Memoized request function first checks to make sure the cache
            // has not been invalidated underneath (first call will always be
            // empty since we have never created the request)
            (mockModificationWatchpointManager.hasModificationWatchpointRequest _)
              .expects(className, fieldName)
              .returning(false).once()
            (mockModificationWatchpointManager.hasModificationWatchpointRequest _)
              .expects(className, fieldName)
              .returning(true).once()

            // NOTE: Expect the request to be created with a unique id
            (mockModificationWatchpointManager.createModificationWatchpointRequestWithId _).expects(
              TestRequestId,
              className,
              fieldName,
              uniqueIdProperty +: arguments
            ).returning(Success("")).once()

            // NOTE: Pipeline adds an event handler id to its metadata
            def newEventPipeline(id: String) = Pipeline.newPipeline(
              classOf[(Event, Seq[JDIEventDataResult])]
            ).withMetadata(Map(EventManager.EventHandlerIdMetadataField -> id))

            eventHandlerIds.foreach(id => {
              (mockEventManager.addEventDataStream _)
                .expects(ModificationWatchpointEventType, Seq(uniqueIdPropertyFilter))
                .returning(newEventPipeline(id)).once()
            })
          }

          (mockModificationWatchpointManager.removeModificationWatchpointRequestWithId _)
            .expects(TestRequestId).once()
          eventHandlerIds.foreach(id => {
            (mockEventManager.removeEventHandler _).expects(id).once()
          })
        }

        val p1 = pureModificationWatchpointProfile.tryGetOrCreateModificationWatchpointRequestWithData(
          className,
          fieldName,
          arguments: _*
        )
        val p2 = pureModificationWatchpointProfile.tryGetOrCreateModificationWatchpointRequestWithData(
          className,
          fieldName,
          arguments: _*
        )

        p1.foreach(_.close(now = true, data = Constants.CloseRemoveAll))
      }
    }
  }
}
