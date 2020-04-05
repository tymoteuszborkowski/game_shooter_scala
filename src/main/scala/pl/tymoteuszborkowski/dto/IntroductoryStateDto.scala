package pl.tymoteuszborkowski.dto

import com.fasterxml.jackson.annotation.{JsonCreator, JsonProperty}

@JsonCreator
class IntroductoryStateDto(@JsonProperty("connected") connected: PlayerDto,
                           @JsonProperty("gameState") gameState: GameStateDto) extends Dto{

  def getConnected: PlayerDto = connected

  def getGameState: GameStateDto = gameState


}
