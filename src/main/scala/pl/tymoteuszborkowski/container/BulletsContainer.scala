package pl.tymoteuszborkowski.container

import pl.tymoteuszborkowski.domain.Bullet
import java.util
import java.util.UUID

class BulletsContainer(val bullets: util.ArrayList[Bullet] = new util.ArrayList) extends Container[Bullet] {

  def add(bullet: Bullet): Unit = {
    bullets.add(bullet)
  }

  override def getAll: util.ArrayList[Bullet] = bullets

  def update(): Unit = {
    bullets.removeIf(bullet => !bullet.isInRange || bullet.hasHitSomething)
  }

  def move(delta: Float): Unit = {
    bullets.forEach((bullet) => bullet.move(delta))
  }

  def removeByPlayerId(id: UUID): Unit = {
    bullets.removeIf(_.getShooterId.equals(id))
  }
}

