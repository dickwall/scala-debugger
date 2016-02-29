package org.scaladebugger.api.profiles.traits.vm
import acyclic.file

import com.sun.jdi.event.VMDisconnectEvent
import org.scaladebugger.api.lowlevel.JDIArgument
import org.scaladebugger.api.lowlevel.events.data.JDIEventDataResult
import org.scaladebugger.api.pipelines.Pipeline
import org.scaladebugger.api.pipelines.Pipeline.IdentityPipeline

import scala.util.Try

/**
 * Represents the interface that needs to be implemented to provide
 * vm disconnect functionality for a specific debug profile.
 */
trait VMDisconnectProfile {
  /** Represents a vm death event and any associated data. */
  type VMDisconnectEventAndData = (VMDisconnectEvent, Seq[JDIEventDataResult])

  /**
   * Constructs a stream of vm disconnect events.
   *
   * @param extraArguments The additional JDI arguments to provide
   *
   * @return The stream of vm disconnect events
   */
  def onVMDisconnect(
    extraArguments: JDIArgument*
  ): Try[IdentityPipeline[VMDisconnectEvent]] = {
    onVMDisconnectWithData(extraArguments: _*).map(_.map(_._1).noop())
  }

  /**
   * Constructs a stream of vm disconnect events.
   *
   * @param extraArguments The additional JDI arguments to provide
   *
   * @return The stream of vm disconnect events and any retrieved data based on
   *         requests from extra arguments
   */
  def onVMDisconnectWithData(
    extraArguments: JDIArgument*
  ): Try[IdentityPipeline[VMDisconnectEventAndData]]

  /**
   * Constructs a stream of vm disconnect events.
   *
   * @param extraArguments The additional JDI arguments to provide
   *
   * @return The stream of vm disconnect events
   */
  def onUnsafeVMDisconnect(
    extraArguments: JDIArgument*
  ): IdentityPipeline[VMDisconnectEvent] = {
    onVMDisconnect(extraArguments: _*).get
  }

  /**
   * Constructs a stream of vm disconnect events.
   *
   * @param extraArguments The additional JDI arguments to provide
   *
   * @return The stream of vm disconnect events and any retrieved data based on
   *         requests from extra arguments
   */
  def onUnsafeVMDisconnectWithData(
    extraArguments: JDIArgument*
  ): IdentityPipeline[VMDisconnectEventAndData] = {
    onVMDisconnectWithData(extraArguments: _*).get
  }
}
