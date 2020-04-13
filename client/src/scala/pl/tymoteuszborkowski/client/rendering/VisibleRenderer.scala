package pl.tymoteuszborkowski.client.rendering

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import pl.tymoteuszborkowski.domain.Visible


class VisibleRenderer(val visible: Visible) extends Renderer {

  def render(shapeRenderer: ShapeRenderer): Unit = {
    shapeRenderer.setColor(visible.getColor)
    shapeRenderer.polygon(visible.getShape.getTransformedVertices)
  }
  
}
