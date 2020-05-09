package pl.tymoteuszborkowski.vfc.attributes

import pl.tymoteuszborkowski.vfc.attributes.Health.MaxHealth

case class Health(var level: Int = MaxHealth) extends ConsistencyAttribute {
  override def relativeLevelOfAttribute(): Float = level.toFloat / MaxHealth
}

object Health {
  val MaxHealth = 10
}