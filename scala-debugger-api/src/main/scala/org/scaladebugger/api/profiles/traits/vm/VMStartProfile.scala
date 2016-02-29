package org.scaladebugger.api.profiles.traits.vm
import acyclic.file

import com.sun.jdi.event.VMStartEvent
import org.scaladebugger.api.lowlevel.JDIArgument
import org.scaladebugger.api.lowlevel.events.data.JDIEventDataResult
import org.scaladebugger.api.pipelines.Pipeline
import org.scaladebugger.api.pipelines.Pipeline.IdentityPipeline

import scala.util.Try

/**
 * Represents the interface that needs to be implemented to provide
 * vm start functionality for a specific debug profile.
 */
trait VMStartProfile {
  /** Represents a vm death event and any associated data. */
  type VMStartEventAndData = (VMStartEvent, Seq[JDIEventDataResult])

  /**
   * Constructs a stream of vm start events.
   *
   * @param extraArguments The additional JDI arguments to provide
   *
   * @return The stream of vm start events
   */
  def onVMStart(
    extraArguments: JDIArgument*
  ): Try[IdentityPipeline[VMStartEvent]] = {
    onVMStartWithData(extraArguments: _*).map(_.map(_._1).noop())
  }

  /**
   * Constructs a stream of vm start events.
   *
   * @param extraArguments The additional JDI arguments to provide
   *
   * @return The stream of vm start events and any retrieved data based on
   *         requests from extra arguments
   */
  def onVMStartWithData(
    extraArguments: JDIArgument*
  ): Try[IdentityPipeline[VMStartEventAndData]]

  /**
   * Constructs a stream of vm start events.
   *
   * @param extraArguments The additional JDI arguments to provide
   *
   * @return The stream of vm start events
   */
  def onUnsafeVMStart(
    extraArguments: JDIArgument*
  ): IdentityPipeline[VMStartEvent] = {
    onVMStart(extraArguments: _*).get
  }

  /**
   * Constructs a stream of vm start events.
   *
   * @param extraArguments The additional JDI arguments to provide
   *
   * @return The stream of vm start events and any retrieved data based on
   *         requests from extra arguments
   */
  def onUnsafeVMStartWithData(
    extraArguments: JDIArgument*
  ): IdentityPipeline[VMStartEventAndData] = {
    onVMStartWithData(extraArguments: _*).get
  }
}

