package pl.tymoteuszborkowski.dto.mapper

import pl.tymoteuszborkowski.domain.{Bullet, Player}
import com.badlogic.gdx.math.Vector2
import pl.tymoteuszborkowski.domain.Bullet
import pl.tymoteuszborkowski.dto.BulletDto
import java.util.UUID

import pl.tymoteuszborkowski.container.Container

object BulletMapper {

  def fromBullet(bullet: Bullet): BulletDto = {
    val position = bullet.getPosition
    new BulletDto(bullet.getId.toString, position.x, position.y, bullet.getRotation, bullet.getShooterId.toString)
  }

  def fromDto(dto: BulletDto, playersContainer: Container[Player]): Bullet = {
    val shooter = playersContainer.getById(dto.getShooterId).orElseThrow(() =>
      new RuntimeException("Cannot find Player of id " + dto.getShooterId + " to create a Bullet."))
    new Bullet(UUID.fromString(dto.getId), shooter, new Vector2(dto.getX, dto.getY), dto.getRotation)
  }

  def updateByDto(bullet: Bullet, dto: BulletDto): Unit = {
    bullet.setPosition(new Vector2(dto.getX, dto.getY))
  }

}
