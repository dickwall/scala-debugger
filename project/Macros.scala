import sbt._
import Keys._

object Macros {
  /** Version used for paradise and quasiquotes. */
  val macroVersion = "2.1.0"

  /** Macro-specific project settings. */
  val settings = Seq(
    addCompilerPlugin(
      "org.scalamacros" % "paradise" % macroVersion cross CrossVersion.full
    ),

    libraryDependencies <+= scalaVersion(
      "org.scala-lang" % "scala-reflect" % _
    ),

    libraryDependencies ++= (
      if (scalaVersion.value.startsWith("2.10")) Seq(
        "org.scalamacros" %% "quasiquotes" % macroVersion
      ) else Nil
    )
  )
}
