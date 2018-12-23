name := "ae-coding-task"

version := "0.1"

scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
  "org.jsoup" % "jsoup" % "1.11.2",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.8.0",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "org.scalatest" % "scalatest_2.12" % "3.0.5" % "test"
)