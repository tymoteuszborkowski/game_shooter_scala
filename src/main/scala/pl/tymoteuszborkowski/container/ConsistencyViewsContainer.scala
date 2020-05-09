package pl.tymoteuszborkowski.container

import java.util.UUID

import pl.tymoteuszborkowski.domain.{Player, Ship}
import pl.tymoteuszborkowski.vfc.utils.ZoneUtils
import pl.tymoteuszborkowski.vfc.{ConsistencyScale, Observation, ConsistencyIdentifiable, ConsistencyView}

import scala.collection.mutable

class ConsistencyViewsContainer[RemotePlayer <: Player](val playersContainer: PlayersContainer[RemotePlayer],
                                                        val consistencyViews: mutable.Buffer[ConsistencyView] =
                                                        mutable.Buffer.empty[ConsistencyView]) {

  def add(toAdd: ConsistencyView): Unit = {
    consistencyViews.append(toAdd)
  }

  def update(): Unit = consistencyViews.foreach(view => view.updateView())

  def refreshConsistencyViews(): Unit = {
    consistencyViews.clear()
    getAllGameShips
      .foreach(observerShip => {
        val observedShips = getAllGameShips
          .filter(_.owner.id != observerShip.owner.id)
          .map(observedShip => {
            val zone = ZoneUtils.pickCorrespondingZone(observerShip, observedShip)
            Observation(observedShip, ConsistencyScale(), zone)
          })

        consistencyViews.append(ConsistencyView(observerShip.asInstanceOf[ConsistencyIdentifiable], observedShips.toList))
      })
  }

  private def getAllGameShips: mutable.Buffer[Ship] = {
    playersContainer
      .buffer
      .filter(_.getShip.isDefined)
      .map(_.getShip.get)
  }


}
