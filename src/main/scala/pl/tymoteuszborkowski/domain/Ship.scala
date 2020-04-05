package pl.tymoteuszborkowski.domain

import java.time.{Duration, Instant}
import java.util.UUID

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.{Polygon, Vector2}
import pl.tymoteuszborkowski.controls.Controls
import pl.tymoteuszborkowski.utils.Vectors


object Ship {
  private val VERTICES = Array[Float](0, 0, 16, 32, 32, 0, 16, 10)
  private val MAX_SPEED = 2000f
  private val ACCELERATION = 500f
  private val ROTATION = 10f
  private val DRAG = 2f
  private val MIDDLE = new Vector2(16, 16)
  private val BULLET_OUTPUT = new Vector2(16, 32)
  private val SHOT_INTERVAL = Duration.ofMillis(300)

  def getMiddle = new Vector2(MIDDLE)
}

class Ship(val owner: Player,
           val startingPosition: Vector2 = new Vector2(0, 0),
           val startingRotation: Float = 0f) extends Visible {

  private val shape: Polygon = initializeShape()
  private val velocity: Vector2 = new Vector2(0, 0)

  private var rotationVelocity: Float = 0
  private var lastShot = Instant.EPOCH
  private var canShoot = false
  private var wantsToShoot = false

  def control(controls: Controls, delta: Float): Unit = {
    if (controls.forward) moveForwards(delta)
    if (controls.left) rotateLeft(delta)
    if (controls.right) rotateRight(delta)
    wantsToShoot = controls.shoot
  }

  def update(delta: Float): Unit = {
    applyMovement(delta)
    applyShootingPossibility()
  }

  def obtainBullet: Option[Bullet] = {
    if (canShoot && wantsToShoot) {
      lastShot = Instant.now
      return Option(new Bullet(UUID.randomUUID(), owner, bulletStartingPosition, shape.getRotation))
    }
    Option.empty[Bullet]
  }

  override def getColor: Color = owner.getColor

  override def getShape: Polygon = shape

  private def getDirection = Vectors.getDirectionVector(shape.getRotation)

  private def moveForwards(delta: Float): Unit = {
    val direction = getDirection
    velocity.x += delta * Ship.ACCELERATION * direction.x
    velocity.y += delta * Ship.ACCELERATION * direction.y
  }

  private def rotateLeft(delta: Float): Unit = {
    rotationVelocity += delta * Ship.ROTATION
  }

  private def rotateRight(delta: Float): Unit = {
    rotationVelocity -= delta * Ship.ROTATION
  }

  private def applyMovement(delta: Float): Unit = {
    velocity.clamp(0, Ship.MAX_SPEED)
    velocity.x -= delta * Ship.DRAG * velocity.x
    velocity.y -= delta * Ship.DRAG * velocity.y
    rotationVelocity -= delta * Ship.DRAG * rotationVelocity
    val x = delta * velocity.x
    val y = delta * velocity.y
    shape.translate(x, y)
    shape.rotate(rotationVelocity)
  }

  private def applyShootingPossibility(): Unit = {
    canShoot = Instant.now.isAfter(lastShot.plus(Ship.SHOT_INTERVAL))
  }

  private def bulletStartingPosition = new Vector2(shape.getX, shape.getY).add(Ship.BULLET_OUTPUT)

  private def initializeShape(): Polygon = {
    val shape: Polygon = new Polygon(Ship.VERTICES)
    shape.setOrigin(Ship.MIDDLE.x, Ship.MIDDLE.y)
    shape.setPosition(startingPosition.x, startingPosition.y)
    shape.setRotation(startingRotation)

    shape
  }

}
