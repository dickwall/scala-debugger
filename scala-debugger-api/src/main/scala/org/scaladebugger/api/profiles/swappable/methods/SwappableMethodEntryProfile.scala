package org.scaladebugger.api.profiles.swappable.methods

import org.scaladebugger.api.lowlevel.JDIArgument
import org.scaladebugger.api.lowlevel.methods.MethodEntryRequestInfo
import org.scaladebugger.api.pipelines.Pipeline.IdentityPipeline
import org.scaladebugger.api.profiles.swappable.SwappableDebugProfile
import org.scaladebugger.api.profiles.traits.methods.MethodEntryProfile

import scala.util.Try

/**
 * Represents a swappable profile for method entry events that redirects the
 * invocation to another profile.
 */
trait SwappableMethodEntryProfile extends MethodEntryProfile {
  this: SwappableDebugProfile =>

  override def onMethodEntryWithData(
    className: String,
    methodName: String,
    extraArguments: JDIArgument*
  ): Try[IdentityPipeline[MethodEntryEventAndData]] = {
    withCurrentProfile.onMethodEntryWithData(
      className,
      methodName,
      extraArguments: _*
    )
  }

  override def methodEntryRequests: Seq[MethodEntryRequestInfo] = {
    withCurrentProfile.methodEntryRequests
  }
}
