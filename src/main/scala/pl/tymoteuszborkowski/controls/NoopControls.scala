package pl.tymoteuszborkowski.controls

class NoopControls extends Controls {
  override def forward = false

  override def left = false

  override def right = false

  override def shoot = false
}