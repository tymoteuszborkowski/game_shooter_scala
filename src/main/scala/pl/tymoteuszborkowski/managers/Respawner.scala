package pl.tymoteuszborkowski.managers

import com.badlogic.gdx.math.Vector2
import pl.tymoteuszborkowski.container.Container
import pl.tymoteuszborkowski.domain.{Player, Ship}

import scala.util.Random

object Respawner {
  private val random = new Random
}

class Respawner[PlayerType <: Player](val playersContainer: Container[PlayerType],
                val widthBound: Float,
                val heightBound: Float) {

  def respawn(): Unit = {
    playersContainer
      .stream
      .filter(player => player.getShip.isEmpty)
      .forEach(respawnFor)
  }
  def respawnFor(player: PlayerType): Unit = {
    player.setShip(new Ship(player, randomRespawnPoint, 0))
  }
  private def randomRespawnPoint =
    new Vector2(Respawner.random.nextInt(widthBound.round), Respawner.random.nextInt(heightBound.round))


}