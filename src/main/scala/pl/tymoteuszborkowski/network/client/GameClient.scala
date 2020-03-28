package pl.tymoteuszborkowski.network.client

import java.io.IOException

import com.esotericsoftware.kryonet.{Client, EndPoint}
import pl.tymoteuszborkowski.common.Network
import pl.tymoteuszborkowski.network.common.Packets
import pl.tymoteuszborkowski.screen.GameScreen

class GameClient(ipAddress: String, tcpPort: Int, udpPort: Int, gameScreen: GameScreen) {

  var client: Client =  new Client
  var game: GameScreen = _
  private var id = 0

  def this(gameScreen: GameScreen) {
    this(Network.SERVER_IP, Network.SERVER_TCP_PORT, Network.SERVER_UDP_PORT, gameScreen)
  }

  private def register(endPoint: EndPoint): Unit = {
    val kyro = endPoint.getKryo
    kyro.register(classOf[Packets.GetClientId])
    kyro.register(classOf[Packets.AddPlayer])
  }

  @throws[IOException]
  def run(): Unit = {
    client.start
    client.addListener(new PacketHandler(this))
    client.connect(5000, ipAddress, tcpPort, udpPort)
    sendGetClientId()
  }

  def sendGetClientId(): Unit = {
    client.sendTCP(new Packets.GetClientId)
  }

  def sendAddPlayer(): Unit = {
    client.sendTCP(new Packets.AddPlayer)
  }

  def getId: Int = id

  def setId(id: Int): Unit = {
    this.id = id
  }

}
