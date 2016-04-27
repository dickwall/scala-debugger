package test

import com.sun.jdi.ThreadReference
import com.sun.jdi.request.EventRequest
import org.scaladebugger.api.lowlevel.breakpoints.BreakpointRequestInfo
import org.scaladebugger.api.lowlevel.classes.{ClassUnloadRequestInfo, ClassPrepareRequestInfo}
import org.scaladebugger.api.lowlevel.exceptions.ExceptionRequestInfo
import org.scaladebugger.api.lowlevel.methods.{MethodExitRequestInfo, MethodEntryRequestInfo}
import org.scaladebugger.api.lowlevel.monitors.{MonitorWaitRequestInfo, MonitorWaitedRequestInfo, MonitorContendedEnterRequestInfo, MonitorContendedEnteredRequestInfo}
import org.scaladebugger.api.lowlevel.requests.{JDIRequestProcessor, JDIRequestArgument}
import org.scaladebugger.api.lowlevel.steps.StepRequestInfo
import org.scaladebugger.api.lowlevel.threads.{ThreadStartRequestInfo, ThreadDeathRequestInfo}
import org.scaladebugger.api.lowlevel.vm.VMDeathRequestInfo
import org.scaladebugger.api.lowlevel.watchpoints.{ModificationWatchpointRequestInfo, AccessWatchpointRequestInfo}

/**
 * Provides helpers to create stubbed request info objects since ScalaMock
 * cannot mock non-empty constructors.
 */
object RequestInfoBuilder {
  // TEST CONSTANTS
  val TestRequestId: String = java.util.UUID.randomUUID().toString
  val TestIsPending: Boolean = false
  val TestIsShortFileName: Boolean = false
  val TestFileName: String = "some/file/name.scala"
  val TestLineNumber: Int = 999
  val TestClassName: String = "some.class.name"
  val TestMethodName: String = "someMethodName"
  val TestFieldName: String = "someFieldName"
  val TestNotifyCaught: Boolean = true
  val TestNotifyUncaught: Boolean = false
  val TestRemoveExistingRequests: Boolean = true
  val TestThreadReference: ThreadReference = null
  val TestSize: Int = 1
  val TestDepth: Int = 2
  val TestJDIRequestArgument: JDIRequestArgument = new JDIRequestArgument {
    override def toProcessor: JDIRequestProcessor = new JDIRequestProcessor {
      override def process(eventRequest: EventRequest): EventRequest = eventRequest
      override val argument: JDIRequestArgument = TestJDIRequestArgument
    }
  }
  val TestExtraArguments: Seq[JDIRequestArgument] = Seq(TestJDIRequestArgument)

  // TEST CREATION METHODS
  def newBreakpointRequestInfo(): BreakpointRequestInfo = BreakpointRequestInfo(
    requestId       = TestRequestId,
    isPending       = TestIsPending,
    isShortFileName = TestIsShortFileName,
    fileName        = TestFileName,
    lineNumber      = TestLineNumber,
    extraArguments  = TestExtraArguments
  )

  def newClassPrepareRequestInfo(): ClassPrepareRequestInfo = ClassPrepareRequestInfo(
    requestId       = TestRequestId,
    isPending       = TestIsPending,
    extraArguments  = TestExtraArguments
  )

  def newClassUnloadRequestInfo(): ClassUnloadRequestInfo = ClassUnloadRequestInfo(
    requestId       = TestRequestId,
    isPending       = TestIsPending,
    extraArguments  = TestExtraArguments
  )

  def newExceptionRequestInfo(): ExceptionRequestInfo = ExceptionRequestInfo(
    requestId       = TestRequestId,
    isPending       = TestIsPending,
    className       = TestClassName,
    notifyCaught    = TestNotifyCaught,
    notifyUncaught  = TestNotifyUncaught,
    extraArguments  = TestExtraArguments
  )

  def newMethodEntryRequestInfo(): MethodEntryRequestInfo = MethodEntryRequestInfo(
    requestId       = TestRequestId,
    isPending       = TestIsPending,
    className       = TestClassName,
    methodName      = TestMethodName,
    extraArguments  = TestExtraArguments
  )

  def newMethodExitRequestInfo(): MethodExitRequestInfo = MethodExitRequestInfo(
    requestId       = TestRequestId,
    isPending       = TestIsPending,
    className       = TestClassName,
    methodName      = TestMethodName,
    extraArguments  = TestExtraArguments
  )

  def newMonitorContendedEnteredRequestInfo(): MonitorContendedEnteredRequestInfo = MonitorContendedEnteredRequestInfo(
    requestId       = TestRequestId,
    isPending       = TestIsPending,
    extraArguments  = TestExtraArguments
  )

  def newMonitorContendedEnterRequestInfo(): MonitorContendedEnterRequestInfo = MonitorContendedEnterRequestInfo(
    requestId       = TestRequestId,
    isPending       = TestIsPending,
    extraArguments  = TestExtraArguments
  )

  def newMonitorWaitedRequestInfo(): MonitorWaitedRequestInfo = MonitorWaitedRequestInfo(
    requestId       = TestRequestId,
    isPending       = TestIsPending,
    extraArguments  = TestExtraArguments
  )

  def newMonitorWaitRequestInfo(): MonitorWaitRequestInfo = MonitorWaitRequestInfo(
    requestId       = TestRequestId,
    isPending       = TestIsPending,
    extraArguments  = TestExtraArguments
  )

  def newStepRequestInfo(): StepRequestInfo = StepRequestInfo(
    requestId               = TestRequestId,
    isPending               = TestIsPending,
    removeExistingRequests  = TestRemoveExistingRequests,
    threadReference         = TestThreadReference,
    size                    = TestSize,
    depth                   = TestDepth,
    extraArguments          = TestExtraArguments
  )

  def newThreadDeathRequestInfo(): ThreadDeathRequestInfo = ThreadDeathRequestInfo(
    requestId       = TestRequestId,
    isPending       = TestIsPending,
    extraArguments  = TestExtraArguments
  )

  def newThreadStartRequestInfo(): ThreadStartRequestInfo = ThreadStartRequestInfo(
    requestId       = TestRequestId,
    isPending       = TestIsPending,
    extraArguments  = TestExtraArguments
  )

  def newVMDeathRequestInfo(): VMDeathRequestInfo = VMDeathRequestInfo(
    requestId       = TestRequestId,
    isPending       = TestIsPending,
    extraArguments  = TestExtraArguments
  )

  def newAccessWatchpointRequestInfo(): AccessWatchpointRequestInfo = AccessWatchpointRequestInfo(
    requestId       = TestRequestId,
    isPending       = TestIsPending,
    className       = TestClassName,
    fieldName       = TestFieldName,
    extraArguments  = TestExtraArguments
  )

  def newModificationWatchpointRequestInfo(): ModificationWatchpointRequestInfo = ModificationWatchpointRequestInfo(
    requestId       = TestRequestId,
    isPending       = TestIsPending,
    className       = TestClassName,
    fieldName       = TestFieldName,
    extraArguments  = TestExtraArguments
  )
}
