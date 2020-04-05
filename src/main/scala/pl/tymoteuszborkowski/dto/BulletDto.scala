package pl.tymoteuszborkowski.dto

import com.fasterxml.jackson.annotation.{JsonCreator, JsonProperty}

@JsonCreator
class BulletDto(@JsonProperty("id") id: String,
                @JsonProperty("x") x: Float,
                @JsonProperty("y") y: Float,
                @JsonProperty("rotation") rotation: Float,
                @JsonProperty("shooterId") shooterId: String) extends Dto {

  def getId: String = id

  def getX: Float = x

  def getY: Float = y

  def getRotation: Float = rotation

  def getShooterId: String = shooterId

}
