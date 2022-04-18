import Dependencies._

ThisBuild / scalaVersion     := "2.13.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

libraryDependencies += "com.bot4s" %% "telegram-core" % "5.4.1"
libraryDependencies += "com.softwaremill.sttp.client3" %% "httpclient-backend" % "3.5.1"
libraryDependencies += "org.postgresql" % "postgresql" % "42.2.2"
libraryDependencies += "io.github.nafg.slick-migration-api" %% "slick-migration-api" % "0.8.2"

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "3.3.3",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.3"
)

lazy val root = (project in file("."))
  .settings(
    name := "social-credit-bot",
    libraryDependencies += scalaTest % Test
  )



// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
