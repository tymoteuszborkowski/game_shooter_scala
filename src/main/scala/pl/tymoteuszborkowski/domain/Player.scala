package pl.tymoteuszborkowski.domain

import java.util.UUID

import com.badlogic.gdx.graphics.Color
import pl.tymoteuszborkowski.controls.Controls

class Player(val id: UUID, val controls: Controls, val color: Color) extends Identifiable {

  private var ship: Option[Ship] = Option.empty


  def setShip(ship: Ship): Unit = {
    this.ship = Option(ship)
  }

  def noticeHit(): Unit = {
    this.ship = Option.empty
  }

  def update(delta: Float): Unit = {
    ship match {
      case Some(value) => value.control(controls, delta)
        value.update(delta)
      case None =>
    }
  }

  def getShip: Option[Ship] = ship

  def getColor: Color = color

  import java.util.UUID

  def getId: UUID = id
}

object Player {
  val PossibleColors: List[Color] = List(Color.WHITE, Color.GRAY, Color.BLUE, Color.GREEN,
    Color.ORANGE, Color.LIGHT_GRAY)
}