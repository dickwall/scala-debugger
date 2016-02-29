package org.scaladebugger.api.profiles.traits.info
//import acyclic.file

import scala.util.Try

/**
 * Represents the interface for thread-based interaction.
 */
trait ThreadInfoProfile {
  /**
   * Represents the unique id of this thread.
   *
   * @return The unique id as a long
   */
  def uniqueId: Long

  /**
   * Represents the name of the thread.
   *
   * @return The thread name as a string
   */
  def name: String

  /**
   * Retrieves profiles for all frames in the stack.
   *
   * @return Success of collection of frame profiles, otherwise a failure
   */
  def frames: Try[Seq[FrameInfoProfile]] = Try(unsafeFrames)

  /**
   * Retrieves profiles for all frames in the stack.
   *
   * @return The collection of frame profiles
   */
  def unsafeFrames: Seq[FrameInfoProfile]

  /**
   * Returns the total frames held in the current frame stack.
   *
   * @return Success containing the total number of frames, otherwise a failure
   */
  def totalFrames: Try[Int] = Try(unsafeTotalFrames)

  /**
   * Returns the total frames held in the current frame stack.
   *
   * @return The total number of frames
   */
  def unsafeTotalFrames: Int

  /**
   * Retrieves the profile for the specified frame in the stack.
   *
   * @param index The index (starting with 0 being top) of the frame whose
   *              profile to retrieve
   * @return Success containing the new frame profile instance, otherwise
   *         a failure
   */
  def withFrame(index: Int): Try[FrameInfoProfile] = Try(withUnsafeFrame(index))

  /**
   * Retrieves the profile for the specified frame in the stack.
   *
   * @param index The index (starting with 0 being top) of the frame whose
   *              profile to retrieve
   * @return The new frame profile instance
   */
  def withUnsafeFrame(index: Int): FrameInfoProfile

  /**
   * Retrieves the profile for the top (current) frame in the stack.
   *
   * @return Success containing the new frame profile instance, otherwise
   *         a failure
   */
  def withTopFrame: Try[FrameInfoProfile] = withFrame(0)

  /**
   * Retrieves the profile for the top (current) frame in the stack.
   *
   * @return The new frame profile instance
   */
  def withUnsafeTopFrame: FrameInfoProfile = withUnsafeFrame(0)
}
