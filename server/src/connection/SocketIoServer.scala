package connection

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
import pl.tymoteuszborkowski.dto._

class SocketIoServer extends Server {

  private val logger = LoggerFactory.getLogger(classOf[SocketIoServer])
  private var socketio: SocketIOServer = _
  private var playerJoinedHandler: Consumer[PlayerDto] = _
  private var playerSentControlsHandler: BiConsumer[UUID, ControlsDto] = _
  private var playerLeftHandler: Consumer[UUID] = _

  def this(host: String, port: Int) = {
    this()
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

  def broadcast(gameState: GameStateDto): Unit = {
    sendEvent(socketio.getBroadcastOperations, Event.GAME_STATE_SENT, gameState)
  }

  def sendIntroductoryDataToConnected(connected: PlayerDto, gameState: GameStateDto): Unit = {
    socketio
      .getAllClients
      .stream
      .filter((client: SocketIOClient) => client.getSessionId == UUID.fromString(connected.getId))
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
      def foo(client: SocketIOClient, json: String, ackSender: AckRequest) = {
        val dto = Dto.fromJsonString(json, classOf[ControlsDto])
        playerSentControlsHandler.accept(client.getSessionId, dto)
      }

      foo(client, json, ackSender)
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
    client.sendEvent(eventName.toString, data.toJsonString)
  }

}
