import sbt.Keys._
import sbt._

object V {
  val ignite = "2.6.0"
  val slf4j = "1.7.10"
  val logback = "1.2.3"
}

object Dependencies {
  val commonDeps = libraryDependencies ++= Seq(
    "org.apache.ignite" % "ignite-core" % V.ignite,
    "org.apache.ignite" % "ignite-spring" % V.ignite,
    "org.apache.ignite" % "ignite-indexing" % V.ignite,

    "org.apache.ignite" % "ignite-slf4j" % V.ignite,
    "org.slf4j" % "slf4j-api" % V.slf4j,
    "ch.qos.logback" % "logback-classic" % V.logback,

    "org.scalatest" %% "scalatest" % "3.0.4" % Test,
    "org.testcontainers" % "testcontainers" % "1.9.0" % Test
  )
}