name := """play2learn"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "3.3.3"

// ThisBuild / scalacOptions += "-Ysemanticdb"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.1" % Test
libraryDependencies += "com.github.t3hnar" % "scala-bcrypt_2.13" % "4.3.0"
// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"

libraryDependencies += "org.playframework" %% "play-slick" % "6.1.0"
libraryDependencies += "org.postgresql" % "postgresql" % "42.7.3"