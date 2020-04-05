package pl.tymoteuszborkowski.dto.mapper

import java.util
import java.util.stream.Collectors.toList
import scala.collection.JavaConverters._
import pl.tymoteuszborkowski.container.Container
import pl.tymoteuszborkowski.domain.{Bullet, Player}
import pl.tymoteuszborkowski.dto.{BulletDto, GameStateDto, PlayerDto}


object GameStateMapper {

  def fromState[T <: Player](players: Container[T], bullets: Container[Bullet]): GameStateDto = {
    val playerDtos: util.List[PlayerDto] = players.getAll.asScala.map(PlayerMapper.fromPlayer).toList.asJava
    val bulletDtos: util.List[BulletDto] = bullets.getAll.asScala.map(BulletMapper.fromBullet).toList.asJava
    new GameStateDto(playerDtos, bulletDtos)
  }

}
