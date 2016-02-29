package org.scaladebugger.api.profiles.swappable

import org.scaladebugger.api.profiles.ProfileManager
import org.scaladebugger.api.profiles.traits.DebugProfile

/**
 * Represents the management functionality of swapping debug profiles.
 */
trait SwappableDebugProfileManagement { this: DebugProfile =>
  protected val profileManager: ProfileManager

  @volatile private var currentProfileName = ""

  /**
   * Sets the current profile to the one with the provided name.
   *
   * @param name The name of the profile
   * @return The updated profile
   */
  def use(name: String): DebugProfile = {
    currentProfileName = name
    this
  }

  /**
   * Retrieves the current underlying profile.
   *
   * @return The active underlying profile
   */
  def withCurrentProfile: DebugProfile = withProfile(currentProfileName)

  /**
   * Retrieves the profile with the provided name.
   *
   * @param name The name of the profile
   * @throws AssertionError If the profile is not found
   * @return The debug profile
   */
  @throws[AssertionError]
  def withProfile(name: String): DebugProfile = {
    val profile = profileManager.retrieve(name)

    assert(profile.nonEmpty, s"Profile $name does not exist!")

    profile.get
  }
}
