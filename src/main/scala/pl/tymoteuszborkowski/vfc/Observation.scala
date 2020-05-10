package pl.tymoteuszborkowski.vfc

case class Observation(observed: ConsistencyIdentifiable,
                       consistencyScale: ConsistencyScale,
                       var zoneWithin: Zone) {

  def performedUpdate(): Boolean = {
    if (shouldUpdate()) {
      consistencyScale.refreshScale(observed)
      true
    } else {
      consistencyScale.addLostUpdate()
      false
    }
  }

  private def shouldUpdate(): Boolean = {
    if (consistencyScale.lostUpdates >= zoneWithin.maxConsistencyScale.lostUpdates
        || consistencyScale.delay >= zoneWithin.maxConsistencyScale.delay
        || consistencyScale.difference <= zoneWithin.maxConsistencyScale.difference) {

      return true
    }

    false
  }
}
