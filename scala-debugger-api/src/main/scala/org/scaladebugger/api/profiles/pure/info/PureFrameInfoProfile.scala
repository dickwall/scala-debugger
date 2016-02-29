package org.scaladebugger.api.profiles.pure.info
//import acyclic.file

import com.sun.jdi._
import org.scaladebugger.api.profiles.traits.info.{ThreadInfoProfile, ObjectInfoProfile, VariableInfoProfile, FrameInfoProfile}

import scala.util.{Failure, Try}
import scala.collection.JavaConverters._

/**
 * Represents a pure implementation of a stack frame profile that adds no custom
 * logic on top of the standard JDI.
 *
 * @param stackFrame The reference to the underlying JDI staxk frame instance
 */
class PureFrameInfoProfile(
  private val stackFrame: StackFrame
) extends FrameInfoProfile {
  private lazy val thisObjectProfile = newObjectProfile(stackFrame.thisObject())
  private lazy val currentThreadProfile = newThreadProfile(stackFrame.thread())

  /**
   * Retrieves the object representing 'this' in the current frame scope.
   *
   * @return The profile of this object
   */
  override def withUnsafeThisObject: ObjectInfoProfile = thisObjectProfile

  /**
   * Retrieves the thread associated with this frame.
   *
   * @return The profile of the thread
   */
  override def withUnsafeCurrentThread: ThreadInfoProfile = currentThreadProfile

  /**
   * Retrieves the variable with the specified name from the frame.
   *
   * @param name The name of the variable to retrieve
   * @return Profile of the variable or throws an exception
   */
  override def forUnsafeVariable(name: String): VariableInfoProfile = {
    Try(Option(stackFrame.visibleVariableByName(name)).get)
      .map(newLocalVariableProfile)
      .getOrElse(thisObjectProfile.unsafeField(name))
  }

  /**
   * Retrieves all variables that represent field variables in this frame.
   *
   * @return The collection of variables as their profile equivalents
   */
  override def forUnsafeFieldVariables: Seq[VariableInfoProfile] = {
    thisObjectProfile.unsafeFields
  }

  /**
   * Retrieves all variables in this frame.
   *
   * @return The collection of variables as their profile equivalents
   */
  override def forUnsafeAllVariables: Seq[VariableInfoProfile] =
    forUnsafeLocalVariables ++ forUnsafeFieldVariables

  /**
   * Retrieves all variables that represent local variables in this frame.
   *
   * @return The collection of variables as their profile equivalents
   */
  override def forUnsafeLocalVariables: Seq[VariableInfoProfile] = {
    stackFrame.visibleVariables().asScala.map(newLocalVariableProfile)
  }

  /**
   * Retrieves all variables that do not represent arguments in this frame.
   *
   * @return The collection of variables as their profile equivalents
   */
  override def forUnsafeNonArguments: Seq[VariableInfoProfile] = {
    forUnsafeLocalVariables.filterNot(_.isArgument)
  }

  /**
   * Retrieves all variables that represent arguments in this frame.
   *
   * @return The collection of variables as their profile equivalents
   */
  override def forUnsafeArguments: Seq[VariableInfoProfile] = {
    forUnsafeLocalVariables.filter(_.isArgument)
  }

  protected def newLocalVariableProfile(localVariable: LocalVariable): VariableInfoProfile =
    new PureLocalVariableInfoProfile(stackFrame, localVariable)()

  protected def newObjectProfile(objectReference: ObjectReference): ObjectInfoProfile =
    new PureObjectInfoProfile(stackFrame, objectReference)

  protected def newThreadProfile(threadReference: ThreadReference): ThreadInfoProfile =
    new PureThreadInfoProfile(threadReference)
}
