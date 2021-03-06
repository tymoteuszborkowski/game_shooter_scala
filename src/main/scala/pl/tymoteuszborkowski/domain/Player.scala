package pl.tymoteuszborkowski.domain

import java.util.UUID

import com.badlogic.gdx.graphics.Color
import pl.tymoteuszborkowski.controls.Controls

class Player(val id: UUID,
             var controls: Controls,
             val color: Color) extends Identifiable {

  private var ship: Option[Ship] = Option.empty

  def setShip(ship: Ship): Unit = {
    this.ship = Option(ship)
  }

  def noticeHit(): Unit = {
    if (ship.isDefined && ship.get.health.level <= 0) {
      ship = Option.empty
    } else {
      ship.get.health.level  -= 1
    }
  }

  def update(): Unit = {
    ship match {
      case Some(value) => value.update()
      case None =>
    }
  }

  def move(delta: Float): Unit = {
    ship match {
      case Some(value) =>
        value.control(controls, delta)
        value.move(delta)
        value.update()
      case None =>
    }
  }

  def getShip: Option[Ship] = ship

  def getColor: Color = color

  import java.util.UUID

  def getId: UUID = id

  def getControls: Controls = controls

  def setControls(controls: Controls) = this.controls = controls
}

object Player {
  val PossibleColors: List[Color] = List(Color.WHITE, Color.GRAY, Color.BLUE, Color.GREEN,
    Color.ORANGE, Color.LIGHT_GRAY)
}