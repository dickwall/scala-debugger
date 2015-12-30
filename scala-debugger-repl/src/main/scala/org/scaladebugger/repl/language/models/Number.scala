package org.scaladebugger.repl.language.models

/**
 * Represents an expression that is numeric.
 *
 * @param value The numeric value of the expression
 */
case class Number(value: Int) extends Expression
