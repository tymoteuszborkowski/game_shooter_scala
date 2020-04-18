package pl.tymoteuszborkowski.client.connection

import java.util.function.Consumer

import pl.tymoteuszborkowski.dto._

trait Client {

  def connect(playerDto: PlayerDto): Unit

  def onConnected(handler: Consumer[IntroductoryStateDto]): Unit

  def onOtherPlayerConnected(handler: Consumer[PlayerDto]): Unit

  def onOtherPlayerDisconnected(handler: Consumer[UuidDto]): Unit

  def onGameStateReceived(handler: Consumer[GameStateDto]): Unit

  def sendControls(controlsDto: ControlsDto): Unit

  def isConnected: Boolean

  def lockEventListeners(): Unit

  def unlockEventListeners(): Unit
}
