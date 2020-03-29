package pl.tymoteuszborkowski.domain

import com.badlogic.gdx.math.{Polygon, Rectangle}

class Arena(val width: Float, val height: Float) {

  var bounds: Rectangle = new Rectangle(0, 0, width, height)

  def ensurePlacementWithinBounds(visible: Visible) {
    val shape: Polygon = visible.getShape
    val shapeBounds = shape.getBoundingRectangle
    var x: Float = shape.getX
    var y: Float = shape.getY

    if (x + shapeBounds.width < bounds.x) x = bounds.width
    if (y + shapeBounds.height < bounds.y) y = bounds.height
    if (x > bounds.width) x = bounds.x - shapeBounds.width
    if (y > bounds.height) y = bounds.y - shapeBounds.height
    shape.setPosition(x, y)
  }
}