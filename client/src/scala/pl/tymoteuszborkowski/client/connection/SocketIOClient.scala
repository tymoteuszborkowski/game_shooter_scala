package pl.tymoteuszborkowski.client.connection

import java.net.URISyntaxException
import java.util.function.Consumer

import io.socket.client.{IO, Socket}
import io.socket.emitter.Emitter
import pl.tymoteuszborkowski.connection.Event
import pl.tymoteuszborkowski.connection.Event.Event
import pl.tymoteuszborkowski.dto._

object ConnectionState extends Enumeration {
  type ConnectionState = Value
  val NOT_CONNECTED, CONNECTING, CONNECTED = Value

}

class SocketIOClient(val protocol: String, val host: String, val port: Int) extends Client {
  val url: String = protocol + "://" + host + ":" + port

  try this.socket = IO.socket(url)
  catch {
    case e: URISyntaxException =>
      throw new RuntimeException("Wrong URL provided for socket connection: " + url, e)
  }

  final private var socket: Socket = _
  private var state = ConnectionState.NOT_CONNECTED
  private var playerConnectedHandler: Consumer[IntroductoryStateDto] = _
  private var otherPlayerConnectedHandler: Consumer[PlayerDto] = _
  private var otherPlayerDisconnectedHandler: Consumer[UuidDto] = _
  private var gameStateReceivedHandler: Consumer[GameStateDto] = _

  def connect(playerDto: PlayerDto): Unit = {
    if (state eq ConnectionState.NOT_CONNECTED) {
      state = ConnectionState.CONNECTING
      socket.on(Socket.EVENT_CONNECT, (response: Array[AnyRef]) => emit(socket, Event.PLAYER_CONNECTING, playerDto))
      on(socket, Event.PLAYER_CONNECTED, (response: Array[AnyRef]) => {
        def foo(response: Array[AnyRef]) = {
          val introductoryStateJson = response(0).asInstanceOf[String]
          playerConnectedHandler.accept(Dto.fromJsonString(introductoryStateJson, classOf[IntroductoryStateDto]))
          state = ConnectionState.CONNECTED
          setupEvents()
        }

        foo(response)
      })
      socket.connect
    }
  }

  def onConnected(handler: Consumer[IntroductoryStateDto]): Unit = {
    playerConnectedHandler = handler
  }

  def onOtherPlayerConnected(handler: Consumer[PlayerDto]): Unit = {
    otherPlayerConnectedHandler = handler
  }

  def onOtherPlayerDisconnected(handler: Consumer[UuidDto]): Unit = {
    otherPlayerDisconnectedHandler = handler
  }

  def onGameStateReceived(handler: Consumer[GameStateDto]): Unit = {
    gameStateReceivedHandler = handler
  }

  def sendControls(controlsDto: ControlsDto): Unit = {
    emit(socket, Event.CONTROLS_SENT, controlsDto)
  }

  def isConnected: Boolean = state eq ConnectionState.CONNECTED

  private def setupEvents(): Unit = {
    on(socket, Event.OTHER_PLAYER_CONNECTED, (response: Array[AnyRef]) => {
      def foo(response: Array[AnyRef]) = {
        val gameStateDtoJson = response(0).asInstanceOf[String]
        otherPlayerConnectedHandler.accept(Dto.fromJsonString(gameStateDtoJson, classOf[PlayerDto]))
      }

      foo(response)
    })
    on(socket, Event.OTHER_PLAYER_DISCONNECTED, (response: Array[AnyRef]) => {
      def foo(response: Array[AnyRef]) = {
        val playerIdJson = response(0).asInstanceOf[String]
        otherPlayerDisconnectedHandler.accept(Dto.fromJsonString(playerIdJson, classOf[UuidDto]))
      }

      foo(response)
    })
    on(socket, Event.GAME_STATE_SENT, (response: Array[AnyRef]) => {
      def foo(response: Array[AnyRef]) = {
        val gameStateDtoJson = response(0).asInstanceOf[String]
        gameStateReceivedHandler.accept(Dto.fromJsonString(gameStateDtoJson, classOf[GameStateDto]))
      }

      foo(response)
    })
  }

  private def emit(socket: Socket, eventName: Event, payload: Dto): Unit = {
    socket.emit(eventName.toString, payload.toJsonString)
  }

  private def on(socket: Socket, eventName: Event, handler: Emitter.Listener): Unit = {
    socket.on(eventName.toString, handler)
  }
}

