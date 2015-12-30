package org.scaladebugger.repl.language.models

/**
 * Represents a request to print help information.
 */
case class Help(topic: Option[String]) extends Expression
