package pl.tymoteuszborkowski.server.connection

import java.io.IOException
import java.util
import java.util.UUID
import java.util.function.{BiConsumer, Consumer}

import com.corundumstudio.socketio._
import com.corundumstudio.socketio.listener.{DataListener, ExceptionListenerAdapter}
import io.netty.channel.ChannelHandlerContext
import org.slf4j.LoggerFactory
import pl.tymoteuszborkowski.connection.Event
import pl.tymoteuszborkowski.connection.Event.Event
import pl.tymoteuszborkowski.container.ConsistencyViewsContainer
import pl.tymoteuszborkowski.domain.RemotePlayer
import pl.tymoteuszborkowski.dto._
import pl.tymoteuszborkowski.dto.mapper.IndexedDtoMapper
import pl.tymoteuszborkowski.dto.mapper.IndexedDtoMapper.wrapWithIndex
import pl.tymoteuszborkowski.server.connection.synchronization.StateIndexByClient
import pl.tymoteuszborkowski.utils.Delay

import scala.collection.JavaConverters._

class SocketIoServer(val delay: Delay, val stateIndexByClient: StateIndexByClient) extends Server {

  private val logger = LoggerFactory.getLogger(classOf[SocketIoServer])
  private var socketio: SocketIOServer = _
  private var playerJoinedHandler: Consumer[PlayerDto] = _
  private var playerSentControlsHandler: BiConsumer[UUID, ControlsDto] = _
  private var playerLeftHandler: Consumer[UUID] = _


  //test variables to analyze sent bytes in both methods (old and vector field consistency)
  private var bytesSentVfc: Long = 0
  private var bytesSentOld: Long = 0


  def this(host: String, port: Int, stateIndexByClient: StateIndexByClient, delay: Delay) = {
    this(delay, stateIndexByClient)
    var config = new Configuration()
    config.setHostname(host)
    config.setPort(port)

    config = setupExceptionListener(config)
    socketio = new SocketIOServer(config)
  }

  def start(): Unit = {
    val config = socketio.getConfiguration
    socketio.start()
    logger.info("Game server listening at " + config.getHostname + ":" + config.getPort)
    setupEvents()
  }

  override def onPlayerConnected(handler: Consumer[PlayerDto]): Unit = {
    playerJoinedHandler = handler
  }

  override def onPlayerDisconnected(handler: Consumer[UUID]): Unit = {
    playerLeftHandler = handler
  }

  override def onPlayerSentControls(handler: BiConsumer[UUID, ControlsDto]): Unit = {
    playerSentControlsHandler = handler
  }

  def broadcast(gameState: GameStateDto,
                consistencyViewsContainer: ConsistencyViewsContainer[RemotePlayer]): Unit = {

    socketio.getAllClients.stream.forEach(client => {
      val vfcDto = vectorFieldConsistencyBroadcastDto(gameState, consistencyViewsContainer, client)
      val oldDto = oldBroadcastDto(gameState, client)

      bytesSentVfc += vfcDto.toJsonString.getBytes.length
      bytesSentOld += oldDto.toJsonString.getBytes.length

      sendEvent(client, Event.GAME_STATE_SENT, vfcDto)
    })
  }

  def vectorFieldConsistencyBroadcastDto(gameState: GameStateDto,
                                         consistencyViewsContainer: ConsistencyViewsContainer[RemotePlayer],
                                         client: SocketIOClient): Dto = {

    val clientViewOption = consistencyViewsContainer.consistencyViews.find(_.observer.id.equals(client.getSessionId))
    val indexedDtoOption = clientViewOption.map { clientView =>
      val observerId = clientView.observer.id.toString

      val playersIdsToUpdate: List[String] = clientView
        .observedObjects
        .filter(_.performedUpdate())
        .map(_.observed.id.toString)

      val updatedPlayers = gameState
        .players.asScala
        .filter(player => playersIdsToUpdate.contains(player.id) || player.id == observerId)
        .toList.asJava

      val updatedBullets = gameState
        .bullets
        .asScala
        .filter(bullet => playersIdsToUpdate.contains(bullet.shooterId) || bullet.shooterId == observerId)
        .toList.asJava

      val updatedGameState = gameState.copy(players = updatedPlayers, bullets = updatedBullets)
      wrapWithIndex(updatedGameState, stateIndexByClient.lastIndexFor(client.getSessionId)).asInstanceOf[Dto]
    }

    indexedDtoOption
      .getOrElse(wrapWithIndex(gameState, stateIndexByClient.lastIndexFor(client.getSessionId)).asInstanceOf[Dto])

  }

  def oldBroadcastDto(gameState: GameStateDto, client: SocketIOClient): Dto = {
    IndexedDtoMapper.wrapWithIndex(gameState, stateIndexByClient.lastIndexFor(client.getSessionId)).asInstanceOf[Dto]
  }

  def sendIntroductoryDataToConnected(connected: PlayerDto, gameState: GameStateDto): Unit = {
    socketio
      .getAllClients
      .stream
      .filter((client: SocketIOClient) => client.getSessionId equals UUID.fromString(connected.getId))
      .findAny
      .ifPresent((client: SocketIOClient) =>
        sendEvent(client, Event.PLAYER_CONNECTED, new IntroductoryStateDto(connected, gameState)))
  }

  def notifyOtherPlayersAboutConnected(connected: PlayerDto): Unit = {
    socketio
      .getAllClients
      .stream
      .filter((client: SocketIOClient) => !(client.getSessionId == UUID.fromString(connected.getId)))
      .forEach((client: SocketIOClient) => sendEvent(client, Event.OTHER_PLAYER_CONNECTED, connected))
  }

  private def setupEvents(): Unit = {
    addEventListener(Event.PLAYER_CONNECTING, (client: SocketIOClient, json: String, ackSender: AckRequest) => {
      def foo(client: SocketIOClient, json: String, ackSender: AckRequest) = {
        val connecting = Dto.fromJsonString(json, classOf[PlayerDto])
        val withAssignedId = new PlayerDto(client.getSessionId.toString, connecting.getColor, connecting.getShipDto)
        playerJoinedHandler.accept(withAssignedId)
      }

      foo(client, json, ackSender)
    })

    addEventListener(Event.CONTROLS_SENT, (client: SocketIOClient, json: String, ackSender: AckRequest) => {
      val indexedDto = Dto.fromJsonString(json, classOf[IndexedControlsDto])
      stateIndexByClient.setIndexFor(client.getSessionId, indexedDto.getIndex)
      playerSentControlsHandler.accept(client.getSessionId, indexedDto.getDto)
    })

    socketio.addDisconnectListener((client: SocketIOClient) => {
      def foo(client: SocketIOClient) = {
        val id = client.getSessionId
        playerLeftHandler.accept(id)
        sendEvent(socketio.getBroadcastOperations, Event.OTHER_PLAYER_DISCONNECTED, new UuidDto(id.toString))
      }

      foo(client)
    })
  }

  private def setupExceptionListener(config: Configuration) = { // event exception handling - to keep it simple just throw them as runtime exceptions
    config.setExceptionListener(new ExceptionListenerAdapter() {
      override def onEventException(e: java.lang.Exception, data: util.List[Object], client: SocketIOClient): Unit = {
        throw new RuntimeException(e)
      }

      override

      def onDisconnectException(e: Exception, client: SocketIOClient): Unit = {
        throw new RuntimeException(e)
      }

      override

      def onConnectException(e: Exception, client: SocketIOClient): Unit = {
        throw new RuntimeException(e)
      }

      override

      def exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable): Boolean = { // connection error, log and move along
        if (cause.isInstanceOf[IOException]) {
          logger.warn(cause.getMessage)
          return true
        }
        false
      }
    })
    config
  }

  private def addEventListener(eventName: Event.Event, listener: DataListener[String]): Unit = {
    socketio.addEventListener(eventName.toString, classOf[String], listener)
  }

  private def sendEvent(client: ClientOperations, eventName: Event, data: Dto): Unit = {
    if (eventName == Event.OTHER_PLAYER_DISCONNECTED) {
      println("Bytes summary for Vector field Consistency model: " + bytesSentVfc)
      println("Bytes summary for old method of updating players: " + bytesSentOld)
      
    }
    delay.execute(() => client.sendEvent(eventName.toString, data.toJsonString))
  }

}
