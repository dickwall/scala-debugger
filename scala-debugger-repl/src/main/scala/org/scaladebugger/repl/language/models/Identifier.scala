package org.scaladebugger.repl.language.models

/**
 * Represents an expression that can be identified by a name.
 *
 * @param name The name identifying the expression
 */
case class Identifier(name: String) extends Expression

