package org.scaladebugger.api.profiles.swappable.exceptions
import acyclic.file

import org.scaladebugger.api.lowlevel.JDIArgument
import org.scaladebugger.api.lowlevel.exceptions.ExceptionRequestInfo
import org.scaladebugger.api.pipelines.Pipeline.IdentityPipeline
import org.scaladebugger.api.profiles.swappable.SwappableDebugProfileManagement
import org.scaladebugger.api.profiles.traits.exceptions.ExceptionProfile

import scala.util.Try

/**
 * Represents a swappable profile for exception events that redirects the
 * invocation to another profile.
 */
trait SwappableExceptionProfile extends ExceptionProfile {
  this: SwappableDebugProfileManagement =>

  override def onExceptionWithData(
    exceptionName: String,
    notifyCaught: Boolean,
    notifyUncaught: Boolean,
    extraArguments: JDIArgument*
  ): Try[IdentityPipeline[ExceptionEventAndData]] = {
    withCurrentProfile.onExceptionWithData(
      exceptionName,
      notifyCaught,
      notifyUncaught,
      extraArguments: _*
    )
  }

  override def onAllExceptionsWithData(
    notifyCaught: Boolean,
    notifyUncaught: Boolean,
    extraArguments: JDIArgument*
  ): Try[IdentityPipeline[ExceptionEventAndData]] = {
    withCurrentProfile.onAllExceptionsWithData(
      notifyCaught,
      notifyUncaught,
      extraArguments: _*
    )
  }

  override def exceptionRequests: Seq[ExceptionRequestInfo] = {
    withCurrentProfile.exceptionRequests
  }
}
