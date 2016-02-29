package org.scaladebugger.api.lowlevel.requests.properties
//import acyclic.file

/**
 * Represents the interface for custom properties to implement.
 */
trait CustomPropertyLike {
  /** @return The key used for the property */
  def key: AnyRef

  /** @return The value used for the property */
  def value: AnyRef
}
