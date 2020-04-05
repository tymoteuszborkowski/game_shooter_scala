name := "server"

version := "0.1"

scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
  "com.badlogicgames.gdx" % "gdx-backend-headless" % "1.9.10",
  "com.badlogicgames.gdx" % "gdx-platform" % "1.9.10",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.10.3",
  "com.corundumstudio.socketio" % "netty-socketio" % "1.7.18",
  "org.slf4j" % "slf4j-simple" % "1.7.30" % Test


)
