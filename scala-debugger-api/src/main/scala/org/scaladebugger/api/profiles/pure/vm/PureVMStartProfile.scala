package org.scaladebugger.api.profiles.pure.vm
import acyclic.file

import com.sun.jdi.event.VMStartEvent
import org.scaladebugger.api.lowlevel.JDIArgument
import org.scaladebugger.api.lowlevel.events.EventManager
import org.scaladebugger.api.lowlevel.utils.JDIArgumentGroup
import org.scaladebugger.api.pipelines.Pipeline
import org.scaladebugger.api.pipelines.Pipeline.IdentityPipeline
import org.scaladebugger.api.profiles.traits.vm.VMStartProfile
import org.scaladebugger.api.lowlevel.events.EventType.VMStartEventType

import scala.util.Try

/**
 * Represents a pure profile for vm start events that adds no
 * extra logic on top of the standard JDI.
 */
trait PureVMStartProfile extends VMStartProfile {
  protected val eventManager: EventManager

  /**
   * Constructs a stream of vm start events.
   *
   * @param extraArguments The additional JDI arguments to provide
   *
   * @return The stream of vm start events and any retrieved data based on
   *         requests from extra arguments
   */
  override def onVMStartWithData(
    extraArguments: JDIArgument*
  ): Try[IdentityPipeline[VMStartEventAndData]] = Try {
    val JDIArgumentGroup(_, eArgs, _) = JDIArgumentGroup(extraArguments: _*)

    eventManager
      .addEventDataStream(VMStartEventType, eArgs: _*)
      .map(t => (t._1.asInstanceOf[VMStartEvent], t._2))
      .noop()
  }
}
