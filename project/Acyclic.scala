import sbt.Keys._
import sbt._

object Acyclic {
  /** Acyclic-specific settings. */
  val settings = Seq(
    libraryDependencies += "com.lihaoyi" %% "acyclic" % "0.1.4" % "provided",

    autoCompilerPlugins := true,

    addCompilerPlugin("com.lihaoyi" %% "acyclic" % "0.1.4"),

    libraryDependencies ++= (
      if (scalaVersion.value.startsWith("2.10")) Seq(
        "org.scala-lang" % "scala-compiler" % scalaVersion.value % "provided"
      ) else Nil
    )
  )
}
