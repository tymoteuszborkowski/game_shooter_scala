package pl.tymoteuszborkowski.dto.mapper

import com.badlogic.gdx.math.Vector2
import pl.tymoteuszborkowski.domain.{Player, Ship}
import pl.tymoteuszborkowski.dto.ShipDto

object ShipMapper {

  def fromShip(ship: Ship): ShipDto = {
    val shipPosition = ship.getPosition
    new ShipDto(shipPosition.x, shipPosition.y, ship.getRotation)
  }

  def fromDto(dto: ShipDto, owner: Player): Ship = {
    if (dto == null) return null
    new Ship(owner, new Vector2(dto.getX, dto.getY), dto.getRotation)
  }

  def updateByDto(ship: Ship, dto: ShipDto): Unit = {
    ship.setPosition(new Vector2(dto.getX, dto.getY))
    ship.setRotation(dto.getRotation)
  }


}
