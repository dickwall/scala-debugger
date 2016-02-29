package org.scaladebugger.api.profiles.pure.info
//import acyclic.file

import com.sun.jdi.{VirtualMachine, ThreadReference}
import org.scaladebugger.api.profiles.traits.info.{ThreadInfoProfile, GrabInfoProfile}

import scala.util.{Success, Try}

/**
 * Represents a pure profile for grabbing various information from threads
 * and other objects that adds no extra logic on top of the standard JDI.
 */
trait PureGrabInfoProfile extends GrabInfoProfile {
  protected val _virtualMachine: VirtualMachine

  /**
   * Retrieves a thread profile for the given JDI thread reference.
   *
   * @param threadReference The JDI thread reference with which to wrap in
   *                        a thread info profile
   * @return The new thread info profile
   */
  override def forUnsafeThread(
    threadReference: ThreadReference
  ): ThreadInfoProfile = newThreadProfile(threadReference)

  /**
   * Retrieves a thread profile for the thread reference whose unique id
   * matches the provided id.
   *
   * @param threadId The id of the thread
   * @return The profile of the matching thread, or throws an exception
   */
  override def forUnsafeThread(threadId: Long): ThreadInfoProfile = {
    import scala.collection.JavaConverters._
    _virtualMachine.allThreads().asScala
      .find(_.uniqueID() == threadId)
      .map(newThreadProfile)
      .get
  }

  protected def newThreadProfile(threadReference: ThreadReference): ThreadInfoProfile =
    new PureThreadInfoProfile(threadReference)
}
