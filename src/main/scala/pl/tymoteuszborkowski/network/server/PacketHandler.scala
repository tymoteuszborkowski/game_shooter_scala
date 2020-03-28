package pl.tymoteuszborkowski.network.server

import com.esotericsoftware.kryonet.{Connection, Listener}
import pl.tymoteuszborkowski.common.Player
import pl.tymoteuszborkowski.network.common.Packets

class PacketHandler extends Listener {


  private var server: GameServer = _

  def this(server: GameServer) {
    this()
    this.server = server
  }

  override def received(c: Connection, o: Object): Unit = {
    super.received(c, o)
    o match {
      case id: Packets.GetClientId => handleGetClientId(c, id)
      case player: Packets.AddPlayer => handleAddPlayer(c, player)
      case _ =>
    }
  }

  private def handleGetClientId(c: Connection, packet: Packets.GetClientId): Unit = {
    packet.id = c.getID
    server.sendGetClientId(c, packet)
  }

  private def handleAddPlayer(c: Connection, packet: Packets.AddPlayer): Unit = {
    packet.id = c.getID
    // Testing
    packet.x = 5
    packet.y = 5
    server.players :+ new Player(packet.x, packet.y, packet.id)
    server.sendAddPlayer(c, packet)
    System.out.println("Connection #" + packet.id + " connected!")
  }

  override def disconnected(c: Connection): Unit = {
    super.disconnected(c)
  }

}
