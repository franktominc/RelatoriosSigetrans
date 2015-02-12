name := """RelatoriosSigetrans"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  "org.hibernate" % "hibernate-core" % "4.3.5.Final",
  "org.hibernate" % "hibernate-entitymanager" % "4.3.5.Final",
  "org.postgresql" % "postgresql" % "9.3-1102-jdbc41"
)
