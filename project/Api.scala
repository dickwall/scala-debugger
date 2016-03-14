import sbt.Keys._
import sbt._

object Api {
  /** Api-specific project settings. */
  val settings = Seq(
    // NOTE: Fork needed to avoid mixing in sbt classloader, which is causing
    //       LinkageError to be thrown for JDI-based classes
    fork in Test := true,
    fork in IntegrationTest := true,

    // Run tests in parallel
    // NOTE: Needed to avoid ScalaTest serialization issues
    parallelExecution in Test := true,
    testForkedParallel in Test := true,

    // Run integration tests in parallel
    parallelExecution in IntegrationTest := true,
    testForkedParallel in IntegrationTest := true,

    libraryDependencies ++= Seq(
      "org.slf4j" % "slf4j-api" % "1.7.5",
      "org.slf4j" % "slf4j-log4j12" % "1.7.5" % "test,it",
      "log4j" % "log4j" % "1.2.17" % "test,it",
      "org.scalatest" %% "scalatest" % "3.0.0-M15" % "test,it",
      "org.scalamock" %% "scalamock-scalatest-support" % "3.2.1" % "test,it"
    )
  )
}
