name := "client"

version := "0.1"

scalaVersion := "2.12.8"


libraryDependencies ++= Seq(
  "com.badlogicgames.gdx" % "gdx-backend-lwjgl" % "1.9.10" % Test,
  "io.socket" % "socket.io-client" % "1.0.0"

)
