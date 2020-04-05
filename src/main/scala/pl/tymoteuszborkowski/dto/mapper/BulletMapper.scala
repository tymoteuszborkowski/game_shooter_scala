package pl.tymoteuszborkowski.dto.mapper

import pl.tymoteuszborkowski.domain.Bullet
import pl.tymoteuszborkowski.dto.BulletDto

object BulletMapper {

  def fromBullet(bullet: Bullet): BulletDto = {
    val position = bullet.getPosition
    new BulletDto(bullet.getId.toString, position.x, position.y, bullet.getRotation, bullet.getShooterId.toString)
  }

}
