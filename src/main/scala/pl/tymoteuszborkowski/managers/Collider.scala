package pl.tymoteuszborkowski.managers

import pl.tymoteuszborkowski.container.Container
import pl.tymoteuszborkowski.domain.{Bullet, Player}

class Collider(val playersContainer: Container[Player],
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
