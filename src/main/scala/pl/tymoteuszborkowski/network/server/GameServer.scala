package pl.tymoteuszborkowski.network.server

import java.io.IOException

import com.badlogic.gdx.backends.headless.HeadlessApplication
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.{ApplicationAdapter, Gdx}
import com.esotericsoftware.kryonet.{Connection, EndPoint, Server}
import org.mockito.Mockito
import pl.tymoteuszborkowski.common.{Network, Player}
import pl.tymoteuszborkowski.network.common.Packets


class GameServer extends ApplicationAdapter {

  private var server: Server = _

  var players: List[Player] = _

  def this(tcpPort: Int, udpPort: Int) {
    this()
    server = new Server()
    players = List.empty
    register(server)
    server.bind(tcpPort, udpPort)
    server.addListener(new PacketHandler(this))
  }

  private def register(endPoint: EndPoint): Unit = {
    val kyro = endPoint.getKryo
    kyro.register(classOf[Packets.GetClientId])
    kyro.register(classOf[Packets.AddPlayer])
  }

  def sendGetClientId(c: Connection, packet: Packets.GetClientId): Unit = {
    server.sendToTCP(c.getID, packet)
  }

  def sendAddPlayer(c: Connection, packet: Packets.AddPlayer): Unit = {
    val packetExisting = new Packets.AddPlayer
    // Send information to everyone
    for (p <- players) {
      if (p.getId != c.getID) { // Send new player to existing players
        server.sendToTCP(p.getId, packet)
        // Send this existing player to new player
        packetExisting.x = p.getX
        packetExisting.y = p.getY
        packetExisting.id = p.getId
        server.sendToTCP(c.getID, packetExisting)
      }
      else { // Send new player to themselves
        server.sendToTCP(c.getID, packet)
      }
    }
  }

  def run(): Unit = {
    System.out.println("Server starting ...")
    server.start
    System.out.println("Server started!")
  }

}

object GameServer {

  def main(args: Array[String]) {
    try {
      val server: GameServer = new GameServer(Network.SERVER_TCP_PORT, Network.SERVER_UDP_PORT)
      Gdx.gl = Mockito.mock(classOf[GL20])
      new HeadlessApplication(server)
      server.run()
    } catch {
      case e: IOException =>
        System.out.println("Error: port already in use!")
    }
  }
}
