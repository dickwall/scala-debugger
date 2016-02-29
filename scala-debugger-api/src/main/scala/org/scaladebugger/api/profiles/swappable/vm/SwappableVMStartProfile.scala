package org.scaladebugger.api.profiles.swappable.vm
import acyclic.file

import org.scaladebugger.api.lowlevel.JDIArgument
import org.scaladebugger.api.pipelines.Pipeline.IdentityPipeline
import org.scaladebugger.api.profiles.swappable.SwappableDebugProfileManagement
import org.scaladebugger.api.profiles.traits.vm.VMStartProfile

import scala.util.Try

/**
 * Represents a swappable profile for vm start events that redirects the
 * invocation to another profile.
 */
trait SwappableVMStartProfile extends VMStartProfile {
  this: SwappableDebugProfileManagement =>

  override def onVMStartWithData(
    extraArguments: JDIArgument*
  ): Try[IdentityPipeline[VMStartEventAndData]] = {
    withCurrentProfile.onVMStartWithData(extraArguments: _*)
  }
}
