package org.scaladebugger.api.dsl.vm

import com.sun.jdi.event.VMDeathEvent
import org.scaladebugger.api.lowlevel.JDIArgument
import org.scaladebugger.api.lowlevel.events.data.JDIEventDataResult
import org.scaladebugger.api.pipelines.Pipeline.IdentityPipeline
import org.scaladebugger.api.profiles.traits.vm.VMDeathProfile

import scala.util.Try

/**
 * Wraps a profile, providing DSL-like syntax.
 *
 * @param vmDeathProfile The profile to wrap
 */
class VMDeathDSLWrapper private[dsl] (
  private val vmDeathProfile: VMDeathProfile
) {
  /** Represents a VMDeath event and any associated data. */
  type VMDeathEventAndData = (VMDeathEvent, Seq[JDIEventDataResult])

  /** @see VMDeathProfile#tryGetOrCreateVMDeathRequest(JDIArgument*) */
  def onVMDeath(
    extraArguments: JDIArgument*
  ): Try[IdentityPipeline[VMDeathEvent]] =
    vmDeathProfile.tryGetOrCreateVMDeathRequest(extraArguments: _*)

  /** @see VMDeathProfile#getOrCreateVMDeathRequest(JDIArgument*) */
  def onUnsafeVMDeath(
    extraArguments: JDIArgument*
  ): IdentityPipeline[VMDeathEvent] =
    vmDeathProfile.getOrCreateVMDeathRequest(extraArguments: _*)

  /** @see VMDeathProfile#getOrCreateVMDeathRequestWithData(JDIArgument*) */
  def onUnsafeVMDeathWithData(
    extraArguments: JDIArgument*
  ): IdentityPipeline[VMDeathEventAndData] =
    vmDeathProfile.getOrCreateVMDeathRequestWithData(extraArguments: _*)

  /** @see VMDeathProfile#tryGetOrCreateVMDeathRequestWithData(JDIArgument*) */
  def onVMDeathWithData(
    extraArguments: JDIArgument*
  ): Try[IdentityPipeline[VMDeathEventAndData]] =
    vmDeathProfile.tryGetOrCreateVMDeathRequestWithData(extraArguments: _*)
}
