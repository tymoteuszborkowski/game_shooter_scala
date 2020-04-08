package pl.tymoteuszborkowski.dto.mapper

import java.util.UUID

import com.badlogic.gdx.graphics.Color
import pl.tymoteuszborkowski.controls.{Controls, RemoteControls}
import pl.tymoteuszborkowski.domain.{Player, RemotePlayer}
import pl.tymoteuszborkowski.dto.PlayerDto

object PlayerMapper {

  def fromPlayer(player: Player): PlayerDto =
    new PlayerDto(player.getId.toString, player.getColor.toString,
      player
        .getShip
        .map(ShipMapper.fromShip)
        .orNull)

  def remotePlayerFromDto(dto: PlayerDto): RemotePlayer =
    new RemotePlayer(UUID.fromString(dto.getId), new RemoteControls, Color.valueOf(dto.getColor))


  def localPlayerFromDto(dto: PlayerDto, controls: Controls): Player = {
    val player = new Player(UUID.fromString(dto.getId), controls, Color.valueOf(dto.getColor))
    player.setShip(ShipMapper.fromDto(dto.getShipDto, player))
    player
  }

  def updateByDto(player: Player, dto: PlayerDto): Unit = {
    val currentShip = player.getShip
    val shipDto = dto.getShipDto
    if (currentShip.isDefined && shipDto != null)
      ShipMapper.updateByDto(currentShip.get, shipDto)
    else player.setShip(ShipMapper.fromDto(shipDto, player))
  }


}
