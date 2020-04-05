package pl.tymoteuszborkowski

import java.util.UUID

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.viewport.FillViewport
import com.badlogic.gdx.{Game, Screen}
import pl.tymoteuszborkowski.MainGame._
import pl.tymoteuszborkowski.container.{BulletsContainer, Container, PlayersContainer}
import pl.tymoteuszborkowski.controls.{KeyboardControls, NoopControls}
import pl.tymoteuszborkowski.domain.{Arena, Bullet, Player}
import pl.tymoteuszborkowski.managers.{Collider, Respawner}
import pl.tymoteuszborkowski.rendering.{ContainerRenderer, PlayerRenderer, VisibleRenderer}

class MainGame extends Game {

  private var asteroids: Screen = _

  def create(): Unit = {
    val viewport = new FillViewport(WORLD_WIDTH, WORLD_HEIGHT)
    val shapeRenderer = new ShapeRenderer

    val arena: Arena = new Arena(WORLD_WIDTH, WORLD_HEIGHT)
    val player1: Player = new Player(UUID.randomUUID(), new KeyboardControls, Color.WHITE)
    val player2: Player = new Player(UUID.randomUUID(), new NoopControls, Color.LIGHT_GRAY)
    val bulletsContainer: Container[Bullet] = new BulletsContainer
    val playersContainer: PlayersContainer[Player] = new PlayersContainer
    playersContainer.add(player1)
    playersContainer.add(player2)
    val respawner: Respawner[Player] = new Respawner(playersContainer, WORLD_WIDTH, WORLD_HEIGHT)
    val collider: Collider[Player] = new Collider(playersContainer, bulletsContainer)

    val bulletsRenderer: ContainerRenderer[Bullet] =
      new ContainerRenderer[Bullet](bulletsContainer, bullet => new VisibleRenderer(bullet))
    val playersRenderer: ContainerRenderer[Player] =
      new ContainerRenderer[Player](playersContainer, a => new PlayerRenderer(a))

    asteroids = new GameScreen(
      viewport, shapeRenderer,
      playersContainer, bulletsContainer,
      arena, respawner, collider,
      playersRenderer, bulletsRenderer)


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
