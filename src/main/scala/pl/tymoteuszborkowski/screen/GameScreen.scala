package pl.tymoteuszborkowski.screen

import java.util.concurrent.CopyOnWriteArrayList

import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.{GL20, OrthographicCamera}
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.{Gdx, ScreenAdapter}
import pl.tymoteuszborkowski.common.Player
import pl.tymoteuszborkowski.network.client.GameClient
import pl.tymoteuszborkowski.{GameMap, MainGame}


class GameScreen extends ScreenAdapter {

  private var client: GameClient = _
  private var game: MainGame = _

  var players: CopyOnWriteArrayList[Player] = _
  var player: Player = _

  var camera: OrthographicCamera = _

  private var map: GameMap = _

  private var batch: SpriteBatch = _

  def this(game: MainGame) {
    this()
    this.game = game
    camera = new OrthographicCamera
    camera.setToOrtho(false, Gdx.graphics.getWidth, Gdx.graphics.getHeight)
    camera.update()
    batch = new SpriteBatch
    map = new GameMap("map.tmx", batch)
    players = new CopyOnWriteArrayList[Player]
    try {
      client = new GameClient(this)
      client.run()
    } catch {
      case e: Exception =>

    }
  }

  def update(delta: Float): Unit = {
    if (Gdx.input.isKeyPressed(Keys.LEFT)) player.setX((player.getX - 300 * delta).toInt)
    if (Gdx.input.isKeyPressed(Keys.RIGHT)) player.setX((player.getX + 300 * delta).toInt)
    if (Gdx.input.isKeyPressed(Keys.UP)) player.setY((player.getY + 300 * delta).toInt)
    if (Gdx.input.isKeyPressed(Keys.DOWN)) player.setY((player.getY - 300 * delta).toInt)
    camera.position.set(player.getX, player.getY, 0)
    // Calculate once
    val cameraHalfWidth = camera.viewportWidth / 2
    val cameraHalfHeight = camera.viewportHeight / 2
    camera.position.x = MathUtils.clamp(camera.position.x, cameraHalfWidth, map.getWidth - cameraHalfWidth)
    camera.position.y = MathUtils.clamp(camera.position.y, cameraHalfHeight, map.getHeight - cameraHalfHeight)
    camera.update()
  }

  override def render(delta: Float): Unit = {
    super.render(delta)
    if (player == null) return
    update(delta)
    Gdx.gl.glClearColor(1, 0, 0, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    map.draw(camera)
    batch.begin()
    import scala.collection.JavaConversions._
    for (p <- players) {
      p.draw(batch)
    }
    batch.end()
  }

  override def dispose(): Unit = {
    client = null
    players.clear()
    players = null
    camera = null
    map.dispose
    batch.dispose()
    super.dispose
  }

}
