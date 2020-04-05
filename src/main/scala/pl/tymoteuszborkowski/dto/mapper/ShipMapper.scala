package pl.tymoteuszborkowski.dto.mapper

import pl.tymoteuszborkowski.domain.Ship
import pl.tymoteuszborkowski.dto.ShipDto

object ShipMapper {

  def fromShip(ship: Ship): ShipDto = {
    val shipPosition = ship.getPosition
    new ShipDto(shipPosition.x, shipPosition.y, ship.getRotation)
  }

}
