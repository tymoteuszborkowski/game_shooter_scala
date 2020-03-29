package pl.tymoteuszborkowski.domain

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.{Polygon, Vector2}
import pl.tymoteuszborkowski.utils.Vectors


object Bullet {
  private val VERTICES = Array[Float](0, 0, 2, 0, 2, 2, 0, 2)
  private val SPEED = 500f
  private val RANGE = 400f
}

class Bullet(val shooter: Player,
             val startPosition: Vector2,
             val rotation: Float) extends Visible {

  private val shape = initializeShape()
  private var remainingRange: Float = Bullet.RANGE
  var hasHitSomething: Boolean = false

  override def getColor: Color = shooter.getColor

  override def getShape: Polygon = shape

  def move(delta: Float): Unit = {
    val direction = Vectors.getDirectionVector(shape.getRotation)
    val movement = new Vector2(direction.x * delta * Bullet.SPEED, direction.y * delta * Bullet.SPEED)
    remainingRange -= movement.len
    shape.translate(movement.x, movement.y)
  }

  def isInRange: Boolean = remainingRange > 0

  def noticeHit(): Unit = {
    hasHitSomething = true
  }

  private def initializeShape(): Polygon = {
    val shape: Polygon =  new Polygon(Bullet.VERTICES)
    shape.setPosition(startPosition.x, startPosition.y)
    shape.setRotation(rotation)
    shape.setOrigin(0, -Ship.getMiddle.y)

    shape
  }
}
