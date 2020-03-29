package pl.tymoteuszborkowski.container

import java.util

import pl.tymoteuszborkowski.domain.{Bullet, Player, Ship}

class PlayersContainer(val players: util.ArrayList[Player] = new util.ArrayList[Player]())
  extends Container[Player] {

  def add(toAdd: Player): Unit = {
    players.add(toAdd)
  }

  def getAll: util.ArrayList[Player] = players

  def update(delta: Float): Unit = {
    players.forEach((player) => player.update(delta))
  }

  def streamShips: util.stream.Stream[Ship] =
    stream
      .filter(player => player.getShip.isDefined)
      .map(_.getShip.get)

  def obtainAndStreamBullets: util.stream.Stream[Bullet] =
    streamShips
      .filter(_.obtainBullet.isDefined)
      .map(_.obtainBullet.get)


}
