package pl.tymoteuszborkowski.vfc

case class Range(startPosition: (Int, Int),
                 endPosition: (Int, Int)) {

  def getStartX: Int = startPosition._1

  def getStartY: Int = startPosition._2

  def getEndX: Int = endPosition._1

  def getEndY: Int = endPosition._2

}
