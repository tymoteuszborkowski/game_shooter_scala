package pl.tymoteuszborkowski.domain

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.{Intersector, Polygon, Vector2}

trait Visible {

  def getColor: Color

  def getShape: Polygon

  def collidesWith(anotherVisible: Visible): Boolean =
    Intersector.overlapConvexPolygons(this.getShape, anotherVisible.getShape)

  def getPosition = new Vector2(getShape.getX, getShape.getY)

  def setPosition(position: Vector2): Unit = {
    getShape.setPosition(position.x, position.y)
  }

  def getRotation: Float = getShape.getRotation

  def setRotation(degrees: Float): Unit = {
    getShape.setRotation(degrees)
  }
}