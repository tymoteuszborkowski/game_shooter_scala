import com.badlogic.gdx.Game
import connection.SocketIoServer
import pl.tymoteuszborkowski.MainGame._
import pl.tymoteuszborkowski.container.{BulletsContainer, PlayersContainer}
import pl.tymoteuszborkowski.domain.{Arena, RemotePlayer}
import pl.tymoteuszborkowski.managers.{Collider, Respawner}

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
    val server = new SocketIoServer(host, port)
    asteroids = new AsteroidsServerScreen(server, playersContainer, bulletsContainer, arena, respawner, collider)
    setScreen(asteroids)
  }

  override def dispose(): Unit = {
    asteroids.dispose
  }

}
