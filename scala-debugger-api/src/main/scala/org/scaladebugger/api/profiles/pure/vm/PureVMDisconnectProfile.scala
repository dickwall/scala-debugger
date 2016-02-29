package org.scaladebugger.api.profiles.pure.vm
import acyclic.file

import com.sun.jdi.event.VMDisconnectEvent
import org.scaladebugger.api.lowlevel.JDIArgument
import org.scaladebugger.api.lowlevel.events.EventManager
import org.scaladebugger.api.lowlevel.utils.JDIArgumentGroup
import org.scaladebugger.api.pipelines.Pipeline
import org.scaladebugger.api.pipelines.Pipeline.IdentityPipeline
import org.scaladebugger.api.profiles.traits.vm.VMDisconnectProfile
import org.scaladebugger.api.lowlevel.events.EventType.VMDisconnectEventType

import scala.util.Try

/**
 * Represents a pure profile for vm disconnect events that adds no
 * extra logic on top of the standard JDI.
 */
trait PureVMDisconnectProfile extends VMDisconnectProfile {
  protected val eventManager: EventManager

  /**
   * Constructs a stream of vm disconnect events.
   *
   * @param extraArguments The additional JDI arguments to provide
   *
   * @return The stream of vm disconnect events and any retrieved data based on
   *         requests from extra arguments
   */
  override def onVMDisconnectWithData(
    extraArguments: JDIArgument*
  ): Try[IdentityPipeline[VMDisconnectEventAndData]] = Try {
    val JDIArgumentGroup(_, eArgs, _) = JDIArgumentGroup(extraArguments: _*)

    eventManager
      .addEventDataStream(VMDisconnectEventType, eArgs: _*)
      .map(t => (t._1.asInstanceOf[VMDisconnectEvent], t._2))
      .noop()
  }
}
