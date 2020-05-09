package pl.tymoteuszborkowski.vfc

import pl.tymoteuszborkowski.GameSettings._

object ZoneGenerator {


  def generateFourZones(): Array[Zone] = {
    Array(
      Zone(1, generateRange(4, 0), ConsistencyScale()),
      Zone(2, generateRange(4, 1), ConsistencyScale(0.2F, 5, 0.2F)),
      Zone(3, generateRange(4, 2), ConsistencyScale(0.8F, 15, 0.5F)),
      Zone(4, generateRange(4, 3), ConsistencyScale(1.0F, 25, 0.9F)))
  }


  // not used
  def generateZones(numberOfZones: Int): Array[Zone] = {
    val zones = for {
      i <- 0 until numberOfZones
      range = generateRange(numberOfZones, i)
      consistencyScale = ConsistencyScale(
        delay = squareToRelativeValue(i),
        lostUpdates = (Math.pow(i, 2) * 2).toInt,
        difference = squareToRelativeValue(i))
    } yield Zone(i + 1, range, consistencyScale)

    zones.toArray
  }

  private def generateRange(numberOfZones: Int, index: Int) = {
    val startVelocity = generatePosition(numberOfZones, index + 0)
    val endVelocity = generatePosition(numberOfZones, index + 1)

    Range(startVelocity, endVelocity)
  }

  private def generatePosition(numberOfZones: Int, zoneNumber: Int): (Int, Int) = {
    val startX = (WORLD_WIDTH / numberOfZones) * zoneNumber
    val startY = (WORLD_HEIGHT / numberOfZones) * zoneNumber

    (startX.toInt, startY.toInt)
  }

  private val squareToRelativeValue = (i: Int) => (Math.pow(i, 2).toFloat / 10) * 2


}
