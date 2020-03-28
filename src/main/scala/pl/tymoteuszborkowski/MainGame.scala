package pl.tymoteuszborkowski

import com.badlogic.gdx.Game
import pl.tymoteuszborkowski.screen.GameScreen

class MainGame extends Game {

  def create(): Unit = {
    setScreen(new GameScreen(this))
  }

  override def dispose(): Unit = {
    getScreen.dispose
    super.dispose
  }

}

object MainGame {

  val DEBUGGING: Boolean = true

}
