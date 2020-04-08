package pl.tymoteuszborkowski.rendering


import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import pl.tymoteuszborkowski.domain.{Player, Ship}

import scala.collection.mutable


class PlayerRenderer(val player: Player,
                     val cache: mutable.Map[Ship, Renderer] = new mutable.WeakHashMap[Ship, Renderer])
  extends Renderer {


  def render(shapeRenderer: ShapeRenderer): Unit = {
    player.getShip match {
      case Some(ship) =>
        cache
          .getOrElseUpdate(ship, new VisibleRenderer(ship))
          .render(shapeRenderer)
      case None =>

    }
  }
}

