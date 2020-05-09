package pl.tymoteuszborkowski.vfc.utils

import pl.tymoteuszborkowski.container.ConsistencyViewsContainer
import pl.tymoteuszborkowski.domain.Player

object VfcDebugUtil {

  def printlnVfcViews[RemotePlayer <: Player](consistencyViewsContainer: ConsistencyViewsContainer[RemotePlayer]): Unit = {
    consistencyViewsContainer.consistencyViews.foreach(view => {
      println("---------")
      println("Observer: " + view.observer.id + ", " + view.observer.position)
      println(view.observedObjects.foreach(observation => {
        println("Observed: " + observation.observed.id + ", " + observation.observed.position)
        println(observation.zoneWithin)
        if(observation.consistencyScale.lostUpdates > 0) {

          println("Consistency scale changed: " + observation.consistencyScale)
        }
      }))
      println("---------")
    })
  }

}
