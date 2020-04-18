package pl.tymoteuszborkowski.client.connection.synchronization

import pl.tymoteuszborkowski.dto.{ControlsDto, GameStateDto}

class LocalState(index: Long = 0,
                 delta: Float = 0,
                 controlsDto: ControlsDto,
                 gameStateAfterLoop: GameStateDto) {


  def gameStateMatches(serverState: GameStateDto): Boolean = gameStateAfterLoop == serverState

  def getIndex: Long = index

  def getDelta: Float = delta

  def getControlsDto: ControlsDto = controlsDto

}
