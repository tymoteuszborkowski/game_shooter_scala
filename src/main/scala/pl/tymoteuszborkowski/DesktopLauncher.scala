package pl.tymoteuszborkowski

import com.badlogic.gdx.backends.lwjgl.{LwjglApplication, LwjglApplicationConfiguration}

object DesktopLauncher {
  def main(arg: Array[String]): Unit = {
    val config = new LwjglApplicationConfiguration
    config.title = "Project :: Xeno"
    config.resizable = false
    new LwjglApplication(new MainGame, config)
  }
}
