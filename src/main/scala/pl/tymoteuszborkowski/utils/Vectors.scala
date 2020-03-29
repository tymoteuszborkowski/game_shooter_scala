package pl.tymoteuszborkowski.utils

import com.badlogic.gdx.math.Vector2

object Vectors {


  def getDirectionVector(rotation: Float) =
    new Vector2(-Math.sin(Math.toRadians(rotation)).toFloat, Math.cos(Math.toRadians(rotation)).toFloat)

}
