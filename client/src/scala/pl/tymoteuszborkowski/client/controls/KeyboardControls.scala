package pl.tymoteuszborkowski.client.controls

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys._
import pl.tymoteuszborkowski.controls.Controls

class KeyboardControls extends Controls {

  def forward: Boolean = Gdx.input.isKeyPressed(UP)

  def left: Boolean = Gdx.input.isKeyPressed(LEFT)

  def right: Boolean = Gdx.input.isKeyPressed(RIGHT)

  def shoot: Boolean = Gdx.input.isKeyPressed(SPACE)

}
