import Dependencies._

name := "onair-game"
organization := "com.onairentertainment"
version := "0.1"

scalaVersion := "2.13.6"

libraryDependencies ++= Akka.typed ++ Circe.deps ++ Seq(
  Akka.akkaTestkit,
  Akka.streamTestkit,
  Akka.akkaHttpTestkit,
  Specs.scalaTest % Test
)