import com.badlogic.gdx.ScreenAdapter
import connection.Server
import pl.tymoteuszborkowski.container.{BulletsContainer, PlayersContainer}
import pl.tymoteuszborkowski.domain.{Arena, RemotePlayer}
import pl.tymoteuszborkowski.dto.mapper.{ControlsMapper, GameStateMapper, PlayerMapper}
import pl.tymoteuszborkowski.managers.{Collider, Respawner}

class AsteroidsServerScreen(val server: Server,
                            val playersContainer: PlayersContainer[RemotePlayer],
                            val bulletsContainer: BulletsContainer,
                            val arena: Arena,
                            val respawner: Respawner[RemotePlayer],
                            val collider: Collider[_]) extends ScreenAdapter {

  override def show(): Unit = {
    server.onPlayerConnected(playerDto => {
      val connected = PlayerMapper.remotePlayerFromDto(playerDto)
      respawner.respawnFor(connected)
      val connectedDto = PlayerMapper.fromPlayer(connected)
      val gameStateDto = GameStateMapper.fromState(playersContainer, bulletsContainer)
      server.sendIntroductoryDataToConnected(connectedDto, gameStateDto)
      server.notifyOtherPlayersAboutConnected(connectedDto)
      playersContainer.add(connected)
    })
    server.onPlayerDisconnected(id => {
      playersContainer.removeById(id)
      bulletsContainer.removeByPlayerId(id)
    })

    server.onPlayerSentControls((id, controlsDto) => {
      playersContainer
        .getById(id)
        .ifPresent(sender => ControlsMapper.setRemoteControlsByDto(controlsDto, sender.getRemoteControls))
    })
    server.start()
  }

  override def render(delta: Float): Unit = {
    respawner.respawn()
    collider.checkCollisions()
    playersContainer.update(delta)
    playersContainer.streamShips.forEach(arena.ensurePlacementWithinBounds)
    playersContainer.obtainAndStreamBullets.forEach(bulletsContainer.add)
    bulletsContainer.update(delta)
    bulletsContainer.stream.forEach(arena.ensurePlacementWithinBounds)
    server.broadcast(GameStateMapper.fromState(playersContainer, bulletsContainer))
  }
}
