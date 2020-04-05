package pl.tymoteuszborkowski.controls

class RemoteControls extends Controls {

  private var forwardVar = false
  private var leftVar = false
  private var rightVar = false
  private var shootVar = false

  def forward: Boolean = forwardVar

  def left: Boolean = leftVar

  def right: Boolean = rightVar

  def shoot: Boolean = shootVar

  def setForward(state: Boolean): Unit = {
    forwardVar = state
  }

  def setLeft(state: Boolean): Unit = {
    leftVar = state
  }

  def setRight(state: Boolean): Unit = {
    rightVar = state
  }

  def setShoot(state: Boolean): Unit = {
    shootVar = state
  }
}
