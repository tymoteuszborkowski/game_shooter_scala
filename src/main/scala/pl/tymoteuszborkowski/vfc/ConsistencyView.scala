package pl.tymoteuszborkowski.vfc

case class ConsistencyView(observer: ConsistencyIdentifiable,
                           observedObjects: List[Observation]) {

  def updateView(): Unit =
    observedObjects.foreach(observation => updateZone(observation))

  private def updateZone(observation: Observation): Unit = {
    val zone = ZoneUtils.pickCorrespondingZone(observer, observation.observed)
    observation.zoneWithin = zone
  }

}


