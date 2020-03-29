package pl.tymoteuszborkowski.network.client

import com.esotericsoftware.kryonet.{Connection, Listener}
import pl.tymoteuszborkowski.MainGame
import pl.tymoteuszborkowski.common.Player
import pl.tymoteuszborkowski.network.common.Packets

class PacketHandler extends Listener {

  private var client: GameClient = _

  def this(client: GameClient) {
    this()
    this.client = client
  }

  override def received(c: Connection, o: Object): Unit = {
    super.received(c, o)
    o match {
      case id: Packets.GetClientId => handleGetClientId(id)
      case player: Packets.AddPlayer => handleAddPlayer(player)
      case _ =>
    }
  }

  private def handleGetClientId(packet: Packets.GetClientId): Unit = {
    client.setId(packet.id)
    client.sendAddPlayer()
    if (MainGame.DEBUGGING) System.out.println("Received Packet: GetClientId\n" + "id: " + packet.id)
  }

  private def handleAddPlayer(packet: Packets.AddPlayer): Unit = {
//    val newPlayer = new Player(packet.x, packet.y, packet.id)
//    client.game.players.add(newPlayer)
//    if (packet.id == client.getId) client.game.player = newPlayer
//    if (MainGame.DEBUGGING) System.out.println(
//      "Received Packet: " + "AddPlayer\n" +
//        "x: " + packet.x + "\n" +
//        "y: " + packet.y + "\n" +
//        "id: " + packet.id + "\n")
  }

}
