package pl.tymoteuszborkowski.dto.mapper

import pl.tymoteuszborkowski.controls.RemoteControls
import pl.tymoteuszborkowski.dto.ControlsDto

object ControlsMapper {

  def setRemoteControlsByDto(dto: ControlsDto, controls: RemoteControls): Unit = {
    controls.setForward(dto.getForward)
    controls.setLeft(dto.getLeft)
    controls.setRight(dto.getRight)
    controls.setShoot(dto.getShoot)
  }
}
