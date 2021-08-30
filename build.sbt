name := "tasks"

version := "0.1"

scalaVersion := "2.13.3"

val akkaVersion = "2.6.16"
libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-parallel-collections" % "0.2.0",
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
  "org.scalatest" %% "scalatest" % "3.2.9" % Test,
  "org.mockito" % "mockito-core" % "3.11.2" % Test,
  "org.scalamock" %% "scalamock" % "5.1.0" % Test,
  "org.slf4j" % "slf4j-simple" % "1.7.32" % Test
)