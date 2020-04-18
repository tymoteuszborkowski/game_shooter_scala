package pl.tymoteuszborkowski.client.connection

import java.net.URISyntaxException
import java.util.concurrent.locks.Lock
import java.util.function.Consumer

import io.socket.client.{IO, Socket}
import io.socket.emitter.Emitter
import pl.tymoteuszborkowski.client.connection.synchronization.LocalStateSynchronizer
import pl.tymoteuszborkowski.connection.Event
import pl.tymoteuszborkowski.connection.Event.Event
import pl.tymoteuszborkowski.dto._
import pl.tymoteuszborkowski.dto.mapper.IndexedDtoMapper
import pl.tymoteuszborkowski.utils.Delay

object ConnectionState extends Enumeration {
  type ConnectionState = Value
  val NOT_CONNECTED, CONNECTING, CONNECTED = Value

}

class SocketIOClient(val protocol: String,
                     val host: String,
                     val port: Int,
                     val localStateSynchronizer: LocalStateSynchronizer,
                     val delay: Delay,
                     val eventListenersLock: Lock) extends Client {
  val url: String = protocol + "://" + host + ":" + port
  private var lastReceivedGameStateIndex: Long = -1

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
    if (state equals ConnectionState.NOT_CONNECTED) {
      state = ConnectionState.CONNECTING
      socket.on(Socket.EVENT_CONNECT, (_: Array[Object]) => emit(socket, Event.PLAYER_CONNECTING, playerDto))

      on(socket, Event.PLAYER_CONNECTED, (response: Array[Object]) => {
          val introductoryStateJson = response(0).asInstanceOf[String]
          playerConnectedHandler.accept(Dto.fromJsonString(introductoryStateJson, classOf[IntroductoryStateDto]))
          state = ConnectionState.CONNECTED
          setupEvents()
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
    val index = localStateSynchronizer.getCurrentIndex
    emit(socket, Event.CONTROLS_SENT, IndexedDtoMapper.wrapWithIndex(controlsDto, index))
  }

  def isConnected: Boolean = state eq ConnectionState.CONNECTED

  private def setupEvents(): Unit = {
    on(socket, Event.OTHER_PLAYER_CONNECTED, (response: Array[Object]) => {
      val gameStateDtoJson = response(0).asInstanceOf[String]
      otherPlayerConnectedHandler.accept(Dto.fromJsonString(gameStateDtoJson, classOf[PlayerDto]))
    })

    on(socket, Event.OTHER_PLAYER_DISCONNECTED, (response: Array[Object]) => {
      val playerIdJson = response(0).asInstanceOf[String]
      otherPlayerDisconnectedHandler.accept(Dto.fromJsonString(playerIdJson, classOf[UuidDto]))
    })

    on(socket, Event.GAME_STATE_SENT,  (response: Array[Object]) => {
      val gameStateDtoJson = response(0).asInstanceOf[String]
      val indexedDto = Dto.fromJsonString(gameStateDtoJson, classOf[IndexedGameStateDto])
      if (indexedDto.getIndex <= lastReceivedGameStateIndex) return
      lastReceivedGameStateIndex = indexedDto.getIndex
      gameStateReceivedHandler.accept(indexedDto.getDto)
      localStateSynchronizer.synchronize(indexedDto)
    })
  }

  override def lockEventListeners(): Unit = eventListenersLock.lock()

  override def unlockEventListeners(): Unit = eventListenersLock.unlock()

  private def emit(socket: Socket, eventName: Event, payload: Dto): Unit = {
    delay.execute(() => socket.emit(eventName.toString, payload.toJsonString))
  }

  private def on(socket: Socket, eventName: Event, handler: Emitter.Listener): Unit = {
    socket.on(eventName.toString, (response: Array[Object]) => {
        eventListenersLock.lock
        try handler.call(response(0).asInstanceOf[String])
        finally eventListenersLock.unlock

    })
  }


}

