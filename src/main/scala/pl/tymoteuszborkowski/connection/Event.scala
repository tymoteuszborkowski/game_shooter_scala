package pl.tymoteuszborkowski.connection

object Event extends Enumeration {

  type Event = Value
  val PLAYER_CONNECTING,  PLAYER_CONNECTED, OTHER_PLAYER_CONNECTED, OTHER_PLAYER_DISCONNECTED,
  CONTROLS_SENT, GAME_STATE_SENT = Value

}
