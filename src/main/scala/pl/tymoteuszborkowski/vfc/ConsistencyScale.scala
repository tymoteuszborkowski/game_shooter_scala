package pl.tymoteuszborkowski.vfc

/**
 * Consistency scale allows to specify consistency within the zone
 *
 * @param delay       Time measured from last replica update (in seconds).
 * @param lostUpdates Number of lost replica updates.
 * @param difference  Relative difference between replica contents.
 *                    Captures the effect of updates on the object internal state.
 */
case class ConsistencyScale(var delay: Float = 0,
                            var lostUpdates: Int = 0,
                            var difference: Float = 0) {

  private var lastUpdateTimestamp: Long = 0

  def refreshScale(identifiable: ConsistencyIdentifiable): Unit = {
    delay = 0
    lostUpdates = 0
    difference = identifiable.calculateAverageRelativeAttributeLevel()
    lastUpdateTimestamp = System.currentTimeMillis()
  }

  def addLostUpdate(): Unit = {
    delay += (System.currentTimeMillis() - lastUpdateTimestamp) / 1000
    lostUpdates += 1
  }
}