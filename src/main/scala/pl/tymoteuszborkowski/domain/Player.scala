package pl.tymoteuszborkowski.domain

import com.badlogic.gdx.graphics.Color
import pl.tymoteuszborkowski.controls.Controls


class Player(val controls: Controls, val color: Color) {

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
}