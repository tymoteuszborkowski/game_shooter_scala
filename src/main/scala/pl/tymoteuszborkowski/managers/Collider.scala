package pl.tymoteuszborkowski.managers

import pl.tymoteuszborkowski.container.Container
import pl.tymoteuszborkowski.domain.{Bullet, Player}

class Collider[PlayerType <: Player](val playersContainer: Container[PlayerType],
               val bulletsContainer: Container[Bullet]) {

  def checkCollisions(): Unit = {
    bulletsContainer
      .stream
      .forEach(bullet =>
        playersContainer
          .stream
          .filter(player => player.getShip.isDefined && player.getShip.get.collidesWith(bullet))
          .findFirst()
          .ifPresent(player => {
            player.noticeHit()
            bullet.noticeHit()
          }))
  }
}
