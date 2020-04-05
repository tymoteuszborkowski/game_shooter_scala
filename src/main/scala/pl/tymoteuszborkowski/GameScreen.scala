package pl.tymoteuszborkowski

import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.viewport.Viewport
import com.badlogic.gdx.{Gdx, ScreenAdapter}
import pl.tymoteuszborkowski.container.{Container, PlayersContainer}
import pl.tymoteuszborkowski.domain.{Arena, Bullet, Player}
import pl.tymoteuszborkowski.managers.{Collider, Respawner}
import pl.tymoteuszborkowski.rendering.ContainerRenderer


class GameScreen(val viewport: Viewport,
                 val shapeRenderer: ShapeRenderer,
                 val playersContainer: PlayersContainer[Player],
                 val bulletsContainer: Container[Bullet],
                 val arena: Arena,
                 val respawner: Respawner[Player],
                 val collider: Collider[Player],
                 val playersRenderer: ContainerRenderer[Player],
                 val bulletsRenderer: ContainerRenderer[Bullet]) extends ScreenAdapter {


  override def render(delta: Float): Unit = {
    Gdx.gl.glClearColor(0, 0, 0, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

    respawner.respawn()
    collider.checkCollisions()

    playersContainer.update(delta)
    playersContainer.streamShips.forEach(arena.ensurePlacementWithinBounds)
    playersContainer.obtainAndStreamBullets.forEach(bullet => bulletsContainer.add(bullet))

    bulletsContainer.update(delta)
    bulletsContainer.stream.forEach(arena.ensurePlacementWithinBounds)

    viewport.apply()
    shapeRenderer.setProjectionMatrix(viewport.getCamera.combined)
    shapeRenderer.end()
    shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
    playersRenderer.render(shapeRenderer)
    bulletsRenderer.render(shapeRenderer)
  }

  override def resize(width: Int, height: Int): Unit = {
    viewport.update(width, height, true)
  }

  override def dispose(): Unit = {
    shapeRenderer.dispose()
  }
}
