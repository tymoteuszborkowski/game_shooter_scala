package pl.tymoteuszborkowski.vfc.utils

import pl.tymoteuszborkowski.GameSettings.GameZones
import pl.tymoteuszborkowski.vfc.{ConsistencyIdentifiable, Zone}

object ZoneUtils {

  def pickCorrespondingZone(observer: ConsistencyIdentifiable, observed: ConsistencyIdentifiable): Zone = {
    val xDistance = Math.abs(observer.position._1 - observed.position._1)
    val yDistance = Math.abs(observer.position._2 - observed.position._2)

    GameZones
      .find(_.isInZoneRange(xDistance, yDistance))
      .getOrElse(GameZones.head)
  }

}
