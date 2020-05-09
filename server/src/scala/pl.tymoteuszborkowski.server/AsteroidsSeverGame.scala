package pl.tymoteuszborkowski.server

import com.badlogic.gdx.Game
import pl.tymoteuszborkowski.GameSettings._
import pl.tymoteuszborkowski.container.{BulletsContainer, ConsistencyViewsContainer, PlayersContainer}
import pl.tymoteuszborkowski.domain.{Arena, RemotePlayer}
import pl.tymoteuszborkowski.managers.{Collider, Respawner}
import pl.tymoteuszborkowski.server.connection.SocketIoServer
import pl.tymoteuszborkowski.server.connection.synchronization.StateIndexByClient
import pl.tymoteuszborkowski.utils.Delay

class AsteroidsSeverGame extends Game {

  private var asteroids: AsteroidsServerScreen = _

  def create(): Unit = {
    val arena = new Arena(WORLD_WIDTH, WORLD_HEIGHT)
    val bulletsContainer = new BulletsContainer
    val playersContainer = new PlayersContainer[RemotePlayer]
    val respawner = new Respawner[RemotePlayer](playersContainer, WORLD_WIDTH, WORLD_HEIGHT)
    val collider = new Collider[RemotePlayer](playersContainer, bulletsContainer)

    val env = System.getenv
    val host = env.getOrDefault("HOST", "localhost")
    val port = env.getOrDefault("PORT", "8080").toInt
    val server = new SocketIoServer(host, port, new StateIndexByClient(), new Delay(100))

    val consistencyViewsContainer = new ConsistencyViewsContainer[RemotePlayer](playersContainer)
    asteroids = new AsteroidsServerScreen(server, playersContainer, bulletsContainer, arena, respawner, collider,
      consistencyViewsContainer)
    setScreen(asteroids)
  }

  override def dispose(): Unit = {
    asteroids.dispose
  }

}
