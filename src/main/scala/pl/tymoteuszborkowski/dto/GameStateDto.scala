package pl.tymoteuszborkowski.dto

import java._

import com.fasterxml.jackson.annotation.{JsonCreator, JsonProperty}
import com.fasterxml.jackson.databind.ObjectMapper

@JsonCreator
class GameStateDto(@JsonProperty("players") players: util.List[PlayerDto],
                   @JsonProperty("bullets") bullets: util.List[BulletDto]) extends Dto {


  def getPlayers: util.List[PlayerDto] = players

  def getBullets: util.List[BulletDto] = bullets

}

object GameStateDto {

  val objectMapper: ObjectMapper = new ObjectMapper
}
