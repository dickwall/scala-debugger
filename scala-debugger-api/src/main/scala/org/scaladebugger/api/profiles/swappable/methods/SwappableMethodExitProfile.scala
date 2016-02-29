package org.scaladebugger.api.profiles.swappable.methods
import acyclic.file

import org.scaladebugger.api.lowlevel.JDIArgument
import org.scaladebugger.api.lowlevel.methods.MethodExitRequestInfo
import org.scaladebugger.api.pipelines.Pipeline.IdentityPipeline
import org.scaladebugger.api.profiles.swappable.SwappableDebugProfileManagement
import org.scaladebugger.api.profiles.traits.methods.MethodExitProfile

import scala.util.Try

/**
 * Represents a swappable profile for method exit events that redirects the
 * invocation to another profile.
 */
trait SwappableMethodExitProfile extends MethodExitProfile {
  this: SwappableDebugProfileManagement =>

  override def onMethodExitWithData(
    className: String,
    methodName: String,
    extraArguments: JDIArgument*
  ): Try[IdentityPipeline[MethodExitEventAndData]] = {
    withCurrentProfile.onMethodExitWithData(
      className,
      methodName,
      extraArguments: _*
    )
  }

  override def methodExitRequests: Seq[MethodExitRequestInfo] = {
    withCurrentProfile.methodExitRequests
  }
}
