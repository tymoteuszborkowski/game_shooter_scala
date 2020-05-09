package pl.tymoteuszborkowski.server.connection

import java.util.UUID
import java.util.function.{BiConsumer, Consumer}

import pl.tymoteuszborkowski.container.ConsistencyViewsContainer
import pl.tymoteuszborkowski.domain.RemotePlayer
import pl.tymoteuszborkowski.dto.{ControlsDto, GameStateDto, PlayerDto}

trait Server {

  def start(): Unit

  def onPlayerConnected(handler: Consumer[PlayerDto]): Unit

  def onPlayerDisconnected(handler: Consumer[UUID]): Unit

  def onPlayerSentControls(handler: BiConsumer[UUID, ControlsDto]): Unit

  def broadcast(gameState: GameStateDto, consistencyViewsContainer: ConsistencyViewsContainer[RemotePlayer]): Unit

  def sendIntroductoryDataToConnected(connected: PlayerDto, gameState: GameStateDto): Unit

  def notifyOtherPlayersAboutConnected(connected: PlayerDto): Unit

}
