lazy val commonSettings = Seq(
  version := "1.0-SNAPSHOT",
  scalaVersion := "2.11.5",
  organization := "com.ovoenergy.commons"
)

resolvers ++= Seq(
  "OVO Release" at "http://nexus.ovotech.org.uk:8081/nexus/content/repositories/releases/",
  "OVO Snapshots" at "http://nexus.ovotech.org.uk:8081/nexus/content/repositories/snapshots/")

lazy val validation = (project in file("validation"))
  .settings(name := "commons-validation")
  .settings(commonSettings: _*)
  .settings(libraryDependencies ++= Seq(
  "org.scalaz" %% "scalaz-core" % "7.0.6",
  "joda-time" % "joda-time" % "2.8.1",
  "org.scala-lang" % "scala-reflect" % scalaVersion.value,
  "org.specs2" %% "specs2" % "2.3.13" % "test",
  "org.typelevel" %% "scalaz-specs2" % "0.2" % "test"))