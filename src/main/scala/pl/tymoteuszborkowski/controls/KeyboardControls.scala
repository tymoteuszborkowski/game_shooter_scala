package pl.tymoteuszborkowski.controls

import com.badlogic.gdx.Input.Keys._

class KeyboardControls extends Controls {

  import com.badlogic.gdx.Gdx

  def forward: Boolean = Gdx.input.isKeyPressed(UP)

  def left: Boolean = Gdx.input.isKeyPressed(LEFT)

  def right: Boolean = Gdx.input.isKeyPressed(RIGHT)

  def shoot: Boolean = Gdx.input.isKeyPressed(SPACE)

}
