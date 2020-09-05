package pl.tymoteuszborkowski.server

import com.badlogic.gdx.ScreenAdapter
import pl.tymoteuszborkowski.container.{BulletsContainer, ConsistencyViewsContainer, PlayersContainer}
import pl.tymoteuszborkowski.domain.{Arena, RemotePlayer}
import pl.tymoteuszborkowski.dto.mapper.{ControlsMapper, GameStateMapper, PlayerMapper}
import pl.tymoteuszborkowski.managers.{Collider, Respawner}
import pl.tymoteuszborkowski.server.connection.Server
import pl.tymoteuszborkowski.utils.VfcDebugUtil

class AsteroidsServerScreen(val server: Server,
                            val playersContainer: PlayersContainer[RemotePlayer],
                            val bulletsContainer: BulletsContainer,
                            val arena: Arena,
                            val respawner: Respawner[RemotePlayer],
                            val collider: Collider[_],
                            val consistencyViewsContainer: ConsistencyViewsContainer[RemotePlayer]) extends ScreenAdapter {

  override def show(): Unit = {
    server.onPlayerConnected(playerDto => {
      val connectedPlayer = PlayerMapper.remotePlayerFromDto(playerDto)
      respawner.respawnFor(connectedPlayer)
      val connectedDto = PlayerMapper.fromPlayer(connectedPlayer)
      val gameStateDto = GameStateMapper.fromState(playersContainer, bulletsContainer)
      server.sendIntroductoryDataToConnected(connectedDto, gameStateDto)
      server.notifyOtherPlayersAboutConnected(connectedDto)

      playersContainer.add(connectedPlayer)
      consistencyViewsContainer.refreshConsistencyViews()
      VfcDebugUtil.printlnVfcViews(consistencyViewsContainer) //todo remove
    })
    server.onPlayerDisconnected(id => {
      playersContainer.removeById(id)
      bulletsContainer.removeByPlayerId(id)

      consistencyViewsContainer.refreshConsistencyViews()
    })

    server.onPlayerSentControls((id, controlsDto) => {
      playersContainer
        .getById(id)
        .ifPresent(sender => {
          consistencyViewsContainer.update()
          VfcDebugUtil.printlnVfcViews(consistencyViewsContainer) //todo remove
          ControlsMapper.setRemoteControlsByDto(controlsDto, sender.getRemoteControls)
        })
    })
    server.start()
  }

  override def render(delta: Float): Unit = {
    consistencyViewsContainer.update()
    respawner.respawn()
    collider.checkCollisions()
    playersContainer.move(delta)
    playersContainer.update()
    playersContainer.streamShips.forEach(arena.ensurePlacementWithinBounds)
    playersContainer.obtainAndStreamBullets.forEach(bulletsContainer.add)
    bulletsContainer.move(delta)
    bulletsContainer.update()
    bulletsContainer.stream.forEach(arena.ensurePlacementWithinBounds)
    server.broadcast(GameStateMapper.fromState(playersContainer, bulletsContainer), consistencyViewsContainer)
  }
}
