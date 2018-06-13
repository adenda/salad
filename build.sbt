name := "salad"
organization := "com.adendamedia"


scalaVersion := "2.12.6"

resolvers += "Sonatype Releases" at "https://oss.sonatype.org/service/repositories/releases/"

val testDependencies = Seq(
  "junit" % "junit" % "4.10" % "test",
  "org.scalatest" %% "scalatest"  % "3.0.0" % "test"
)

libraryDependencies ++= Seq(
  "io.lettuce" % "lettuce-core" % "5.0.4.RELEASE",
  "com.typesafe" % "config" % "1.3.1",
  "com.typesafe.akka" %% "akka-actor" % "2.5.13",
  "org.scala-lang.modules" %% "scala-java8-compat" % "0.8.0",
  "org.xerial.snappy" % "snappy-java" % "1.1.2.1",
  "org.slf4j" % "slf4j-api" % "1.7.22"
) ++ testDependencies


pomIncludeRepository := { _ => false }

// Add sonatype repository settings
publishTo := Some(
  if (isSnapshot.value)
    Opts.resolver.sonatypeSnapshots
  else
    Opts.resolver.sonatypeStaging
)

