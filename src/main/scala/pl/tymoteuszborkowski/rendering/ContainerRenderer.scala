package pl.tymoteuszborkowski.rendering


import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import pl.tymoteuszborkowski.container.Container

import scala.collection.mutable

class ContainerRenderer[T](val container: Container[T],
                           val renderer: T => Renderer,
                           val cache: mutable.Map[T, Renderer] = new mutable.WeakHashMap[T, Renderer])
  extends Renderer {

  def render(shapeRenderer: ShapeRenderer): Unit = {
    container.getAll.forEach(thing =>
      cache
        .getOrElseUpdate(thing, renderer.apply(thing))
        .render(shapeRenderer))
  }
}
