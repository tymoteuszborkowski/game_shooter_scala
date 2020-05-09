package pl.tymoteuszborkowski.vfc

case class Range(startVelocity: (Int, Int),
                 endVelocity: (Int, Int)) {

  def getStartX: Int = startVelocity._1

  def getStartY: Int = startVelocity._2

  def getEndX: Int = endVelocity._1

  def getEndY: Int = endVelocity._2

}
