package pl.tymoteuszborkowski.dto

import com.fasterxml.jackson.annotation.JsonProperty

class ShipDto(@JsonProperty("x") x: Float,
              @JsonProperty("y") y: Float,
              @JsonProperty("rotation") rotation: Float) extends Dto {

  def getX: Float = x

  def getY: Float = y

  def getRotation: Float = rotation


}
