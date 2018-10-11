import sbt._
import Dependencies.commonDeps

val commonSettings = Seq(
  name := "jug-nsk-ignite",
  version := "2.6.3",
  scalaVersion := "2.11.11",
  organization := "jug.nsk.ignite",
  scalacOptions ++= Seq(
    "-deprecation",
    "-Ywarn-unused-import",
    "-Xlint:missing-interpolator",
    "-Ywarn-dead-code"
  )
)

test in assembly := {}

val JugNskIgnite = project.in(file("."))
  .settings(commonSettings: _*)
  .settings(commonDeps)

