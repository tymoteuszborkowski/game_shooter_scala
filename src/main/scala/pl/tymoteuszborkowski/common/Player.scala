package pl.tymoteuszborkowski.common

import java.util
import java.util.concurrent.CopyOnWriteArrayList

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.{Rectangle, Vector2}
import pl.tymoteuszborkowski.MainGame


class Player {

  var position: Vector2 = _
  private var bounds: Rectangle = _
  private var id = 0

  def this(x: Int, y: Int, id: Int) {
    this()
    this.id = id
    position = new Vector2(x, y)
    bounds = new Rectangle(position.x, position.y, 32, 32)
  }

  def update(delta: Float): Unit = {
    bounds.setPosition(position)
  }

  def draw(batch: SpriteBatch): Unit = {
    if (MainGame.DEBUGGING) {
      val renderer = new ShapeRenderer
      renderer.setProjectionMatrix(batch.getProjectionMatrix)
      renderer.setAutoShapeType(true)
      renderer.begin()
      renderer.rect(position.x, position.y, bounds.width, bounds.height)
      renderer.end()
    }
  }

  def getPlayerById(id: Int, players: util.ArrayList[Player]): Player = {
    val iter = players.iterator
    while ( {
      iter.hasNext
    }) {
      val p = iter.next
      if (p.getId == id) return p
    }
    null
  }

  def getPlayerById(id: Int, players: CopyOnWriteArrayList[Player]): Player = {
    val iter = players.iterator
    while ( {
      iter.hasNext
    }) {
      val p = iter.next
      if (p.getId == id) return p
    }
    null
  }

  def setPosition(x: Int, y: Int): Unit = {
    position.set(x, y)
  }

  def getX: Int = position.x.toInt

  def setX(x: Int): Unit = {
    this.position.x = x
  }

  def getY: Int = position.y.toInt

  def setY(y: Int): Unit = {
    this.position.y = y
  }

  def getId: Int = this.id

  def setId(id: Int): Unit = {
    this.id = id
  }

}
