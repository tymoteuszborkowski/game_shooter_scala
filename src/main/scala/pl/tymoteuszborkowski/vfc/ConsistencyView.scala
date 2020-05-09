package pl.tymoteuszborkowski.vfc

import pl.tymoteuszborkowski.vfc.utils.ZoneUtils

case class ConsistencyView(observer: ConsistencyIdentifiable,
                           observedObjects: List[Observation]) {

  def updateView(): Unit =
    observedObjects.foreach(observation => {
      updateZone(observation)
      observation.update()
    })


  private def updateZone(observation: Observation): Unit = {
    val zone = ZoneUtils.pickCorrespondingZone(observer, observation.observed)
    observation.zoneWithin = zone
  }

}


