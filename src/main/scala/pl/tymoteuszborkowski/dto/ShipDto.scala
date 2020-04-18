package pl.tymoteuszborkowski.dto

import com.fasterxml.jackson.annotation.{JsonCreator, JsonProperty}

@JsonCreator
case class ShipDto(@JsonProperty("x") x: Float,
                   @JsonProperty("y") y: Float,
                   @JsonProperty("rotation") rotation: Float,
                   @JsonProperty("velocityX") velocityX: Float,
                   @JsonProperty("velocityY") velocityY: Float,
                   @JsonProperty("rotationVelocity") rotationVelocity: Float) extends Dto {

  def getX: Float = x

  def getY: Float = y

  def getRotation: Float = rotation

  def getVelocityX: Float = velocityX

  def getVelocityY: Float = velocityY

  def getRotationVelocity: Float = rotationVelocity


}
