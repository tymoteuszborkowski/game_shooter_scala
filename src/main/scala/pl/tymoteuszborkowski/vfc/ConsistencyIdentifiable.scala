package pl.tymoteuszborkowski.vfc

import java.util.UUID

import pl.tymoteuszborkowski.vfc.attributes.ConsistencyAttribute

trait ConsistencyIdentifiable {

  val id: UUID
  var position: (Float, Float)

  def getAllConsistencyAttributes: List[ConsistencyAttribute]

  def calculateAverageRelativeAttributeLevel(): Float = {
    val relativeValues = getAllConsistencyAttributes
      .map(_.relativeLevelOfAttribute())

    relativeValues.sum / relativeValues.length.toFloat
  }

}
