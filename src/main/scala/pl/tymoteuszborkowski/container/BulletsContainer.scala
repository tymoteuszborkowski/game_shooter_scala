package pl.tymoteuszborkowski.container

import pl.tymoteuszborkowski.domain.Bullet
import java.util

class BulletsContainer(val bullets: util.ArrayList[Bullet] = new util.ArrayList) extends Container[Bullet] {

  def add(bullet: Bullet): Unit = {
    bullets.add(bullet)
  }

  override def getAll: util.ArrayList[Bullet] = bullets

  def update(delta: Float): Unit = {
    bullets.forEach(bullet => bullet.move(delta))
    bullets.removeIf(bullet => !bullet.isInRange || bullet.hasHitSomething)
  }
}

