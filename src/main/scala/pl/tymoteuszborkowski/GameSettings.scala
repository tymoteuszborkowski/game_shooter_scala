package pl.tymoteuszborkowski

import pl.tymoteuszborkowski.vfc.{Zone, ZoneGenerator}

object GameSettings {

  val DEBUGGING: Boolean = true
  val WORLD_WIDTH = 800f
  val WORLD_HEIGHT = 600f

  val GameZones: Array[Zone] = ZoneGenerator.generateZones(4)

}
