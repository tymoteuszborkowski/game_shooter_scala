name := "game_shooter_scala"

version := "0.1"

scalaVersion := "2.12.8"

lazy val core = project in file(".")
lazy val server = project in file("./server")
lazy val client = project in file("./client")


lazy val a = server.dependsOn(core, client)
lazy val b = client.dependsOn(core)
lazy val c = core.aggregate(server, client)

libraryDependencies ++= Seq(
  "com.badlogicgames.gdx" % "gdx-backend-lwjgl" % "1.9.10",
  "com.badlogicgames.gdx" % "gdx-platform" % "1.9.10",
  "com.badlogicgames.gdx" % "gdx" % "1.9.10",
  "com.esotericsoftware" % "kryonet" % "2.22.0-RC1",
  "com.badlogicgames.gdx" % "gdx-backend-headless" % "1.9.10",
  "org.mockito" % "mockito-all" % "1.10.19",
  "org.scala-lang.modules" %% "scala-java8-compat" % "0.9.1",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.10.3",
  "com.corundumstudio.socketio" % "netty-socketio" % "1.7.18"

)
