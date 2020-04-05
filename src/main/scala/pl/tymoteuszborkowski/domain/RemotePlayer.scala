package pl.tymoteuszborkowski.domain

import java.util.UUID

import com.badlogic.gdx.graphics.Color
import pl.tymoteuszborkowski.controls.RemoteControls

class RemotePlayer(override val id: UUID, val remoteControls: RemoteControls, override val color: Color)
  extends Player(id, remoteControls, color) {

  def getRemoteControls: RemoteControls = remoteControls
}
