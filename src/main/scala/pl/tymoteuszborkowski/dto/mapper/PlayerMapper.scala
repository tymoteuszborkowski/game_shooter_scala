package pl.tymoteuszborkowski.dto.mapper

import pl.tymoteuszborkowski.controls.RemoteControls
import pl.tymoteuszborkowski.domain.{Player, RemotePlayer}
import pl.tymoteuszborkowski.dto.PlayerDto
import java.util.UUID

import com.badlogic.gdx.graphics.Color

object PlayerMapper {

  def fromPlayer(player: Player): PlayerDto =
    new PlayerDto(player.getId.toString, player.getColor.toString,
      player
        .getShip
        .map(ShipMapper.fromShip)
        .orNull)

  def remotePlayerFromDto(dto: PlayerDto): RemotePlayer =
    new RemotePlayer(UUID.fromString(dto.getId), new RemoteControls, Color.valueOf(dto.getColor))


}
