package pl.tymoteuszborkowski


import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.tiled.{TiledMap, TmxMapLoader}
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer

class GameMap {

  private var map: TiledMap = _
  private var renderer: OrthogonalTiledMapRenderer = _

  private var width = 0
  private var height = 0

  def this(name: String, batch: SpriteBatch) {
    this()
    map = new TmxMapLoader().load(name)
    renderer = new OrthogonalTiledMapRenderer(map, batch)
    width = map.getProperties.get("width", classOf[Integer]) * 32
    height = map.getProperties.get("height", classOf[Integer]) * 32
  }

  def draw(camera: OrthographicCamera): Unit = {
    renderer.setView(camera)
    renderer.render()
  }

  def dispose(): Unit = {
    map.dispose()
    renderer.dispose()
  }

  def getWidth: Int = width

  def getHeight: Int = height

}
