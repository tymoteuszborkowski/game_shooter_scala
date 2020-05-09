package pl.tymoteuszborkowski.vfc

case class Observation(observed: ConsistencyIdentifiable,
                       consistencyScale: ConsistencyScale,
                       var zoneWithin: Zone) {

  def update(): Unit = {

    if (shouldUpdate()) {
      consistencyScale.refreshScale(observed)
    } else {
      consistencyScale.addLostUpdate()
    }
  }

  def shouldUpdate(): Boolean = {
    if (consistencyScale.lostUpdates >= zoneWithin.maxConsistencyScale.lostUpdates
        || consistencyScale.delay >= zoneWithin.maxConsistencyScale.delay
        || consistencyScale.difference >= zoneWithin.maxConsistencyScale.difference) {

      return true
    }

    false
  }
}
