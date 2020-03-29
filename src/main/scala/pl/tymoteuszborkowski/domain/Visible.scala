package pl.tymoteuszborkowski.domain

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Polygon
import com.badlogic.gdx.math.Intersector

trait Visible {

  def getColor: Color

  def getShape: Polygon

  def collidesWith(anotherVisible: Visible): Boolean =
    Intersector.overlapConvexPolygons(this.getShape, anotherVisible.getShape)
}