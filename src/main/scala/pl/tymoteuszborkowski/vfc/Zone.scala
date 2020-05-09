package pl.tymoteuszborkowski.vfc

case class Zone(id: Int,
                range: Range,
                maxConsistencyScale: ConsistencyScale) {

  def isInZoneRange(x: Float, y: Float): Boolean =
    (x >= range.getStartX && x <= range.getEndX) ||
      (y >= range.getStartY && y <= range.getEndY)
}
