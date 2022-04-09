import Dependencies._

ThisBuild / scalaVersion     := "2.13.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

libraryDependencies += "com.bot4s" %% "telegram-core" % "5.4.1"
libraryDependencies += "com.softwaremill.sttp.client3" %% "async-http-client-backend-future" % "3.5.1"

lazy val root = (project in file("."))
  .settings(
    name := "social-credit-bot",
    libraryDependencies += scalaTest % Test
  )



// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
