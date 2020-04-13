package pl.tymoteuszborkowski.client

import java.util

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.viewport.FillViewport
import com.badlogic.gdx.{Game, Screen}
import pl.tymoteuszborkowski.client.MainGame._
import pl.tymoteuszborkowski.client.connection.{Client, SocketIOClient}
import pl.tymoteuszborkowski.client.controls.KeyboardControls
import pl.tymoteuszborkowski.client.rendering.{ContainerRenderer, PlayerRenderer, VisibleRenderer}
import pl.tymoteuszborkowski.container.{BulletsContainer, Container, PlayersContainer}
import pl.tymoteuszborkowski.controls.Controls
import pl.tymoteuszborkowski.domain.{Bullet, Player}

class MainGame extends Game {

  private var asteroids: Screen = _

  def create(): Unit = {
    val viewport = new FillViewport(WORLD_WIDTH, WORLD_HEIGHT)
    val shapeRenderer = new ShapeRenderer

    val localControls: Controls = new KeyboardControls

    val bulletsContainer: Container[Bullet] = new BulletsContainer
    val playersContainer: PlayersContainer[Player] = new PlayersContainer[Player]

    val bulletsRenderer: ContainerRenderer[Bullet] =
      new ContainerRenderer[Bullet](bulletsContainer, bullet => new VisibleRenderer(bullet))
    val playersRenderer: ContainerRenderer[Player] =
      new ContainerRenderer[Player](playersContainer, a => new PlayerRenderer(a))

    val env: util.Map[String, String] = System.getenv
    val protocol: String = env.getOrDefault("PROTOCOL", "http")
    val host: String = env.getOrDefault("HOST", "localhost")
    val port: Int = env.getOrDefault("PORT", "8080").toInt
    val client: Client = new SocketIOClient(protocol, host, port)

    asteroids = new GameScreen(localControls, client, viewport,
      shapeRenderer, playersContainer, bulletsContainer, playersRenderer, bulletsRenderer)

    setScreen(asteroids)
  }

  override def dispose(): Unit = {
    asteroids.dispose
  }

}

object MainGame {

  val DEBUGGING: Boolean = true
  val WORLD_WIDTH = 800f
  val WORLD_HEIGHT = 600f

}
