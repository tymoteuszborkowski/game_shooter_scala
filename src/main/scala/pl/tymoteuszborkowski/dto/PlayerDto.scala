package pl.tymoteuszborkowski.dto

import com.fasterxml.jackson.annotation.{JsonCreator, JsonProperty}

@JsonCreator
case class PlayerDto(@JsonProperty("id") id: String,
                @JsonProperty("color") color: String,
                @JsonProperty("ship") shipDto: ShipDto) extends Dto{

  def getId: String = id

  def getColor: String = color

  def getShipDto: ShipDto = shipDto

}
