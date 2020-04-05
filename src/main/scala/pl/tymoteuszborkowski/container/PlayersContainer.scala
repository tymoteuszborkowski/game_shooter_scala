package pl.tymoteuszborkowski.container

import java.util

import pl.tymoteuszborkowski.domain.{Bullet, Player, Ship}

class PlayersContainer[PlayerType <: Player](val players: util.ArrayList[PlayerType] = new util.ArrayList[PlayerType]())
  extends Container[PlayerType] {

  def add(toAdd: PlayerType): Unit = {
    players.add(toAdd)
  }

  def getAll: util.ArrayList[PlayerType] = players

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
