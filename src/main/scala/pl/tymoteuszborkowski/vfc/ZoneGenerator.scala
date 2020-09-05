package pl.tymoteuszborkowski.vfc

import pl.tymoteuszborkowski.GameSettings._

object ZoneGenerator {


  def generateFourZones(): Array[Zone] = {
    Array(
      Zone(1, generateRange(0), ConsistencyScale()),
      Zone(2, generateRange(1), ConsistencyScale(0.2F, 5, 0.2F)),
      Zone(3, generateRange(2), ConsistencyScale(0.8F, 15, 0.5F)),
      Zone(4, generateRange(3), ConsistencyScale(1.0F, 25, 0.9F)))
  }

  private def generateRange(index: Int) = {
    val startPosition = generatePosition(index + 0)
    val endPosition = generatePosition(index + 1)

    Range(startPosition, endPosition)
  }

  private def generatePosition(zoneNumber: Int): (Int, Int) = {
    val startX = (WORLD_WIDTH / 4) * zoneNumber
    val startY = (WORLD_HEIGHT / 4) * zoneNumber

    (startX.toInt, startY.toInt)
  }
}
