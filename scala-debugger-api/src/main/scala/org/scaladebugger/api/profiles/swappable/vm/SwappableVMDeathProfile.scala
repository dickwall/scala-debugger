package org.scaladebugger.api.profiles.swappable.vm

import org.scaladebugger.api.lowlevel.JDIArgument
import org.scaladebugger.api.lowlevel.vm.VMDeathRequestInfo
import org.scaladebugger.api.pipelines.Pipeline.IdentityPipeline
import org.scaladebugger.api.profiles.swappable.SwappableDebugProfile
import org.scaladebugger.api.profiles.traits.vm.VMDeathProfile

import scala.util.Try

/**
 * Represents a swappable profile for vm death events that redirects the
 * invocation to another profile.
 */
trait SwappableVMDeathProfile extends VMDeathProfile {
  this: SwappableDebugProfile =>

  override def onVMDeathWithData(
    extraArguments: JDIArgument*
  ): Try[IdentityPipeline[VMDeathEventAndData]] = {
    withCurrentProfile.onVMDeathWithData(extraArguments: _*)
  }

  override def vmDeathRequests: Seq[VMDeathRequestInfo] = {
    withCurrentProfile.vmDeathRequests
  }
}
