package pl.tymoteuszborkowski.client

import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.viewport.Viewport
import com.badlogic.gdx.{Gdx, ScreenAdapter}
import pl.tymoteuszborkowski.client.connection.{Client, ConnectionState}
import pl.tymoteuszborkowski.client.connection.synchronization.LocalStateSynchronizer
import pl.tymoteuszborkowski.client.rendering.ContainerRenderer
import pl.tymoteuszborkowski.container.{BulletsContainer, PlayersContainer}
import pl.tymoteuszborkowski.controls.{Controls, NoopControls}
import pl.tymoteuszborkowski.domain.{Arena, Bullet, Player}
import pl.tymoteuszborkowski.dto.mapper.{BulletMapper, ControlsMapper, GameStateMapper, PlayerMapper}
import pl.tymoteuszborkowski.dto.{BulletDto, PlayerDto}
import pl.tymoteuszborkowski.utils.Randomize

import scala.collection.JavaConverters._

class GameScreen(val localControls: Controls,
                 val client: Client,
                 val viewport: Viewport,
                 val shapeRenderer: ShapeRenderer,
                 val playersContainer: PlayersContainer[Player],
                 val bulletsContainer: BulletsContainer,
                 val playersRenderer: ContainerRenderer[Player],
                 val bulletsRenderer: ContainerRenderer[Bullet],
                 val localStateSynchronizer: LocalStateSynchronizer,
                 val arena: Arena) extends ScreenAdapter {

  var localPlayer: Player = _


  override def show(): Unit = {
    client.onConnected(introductoryStateDto => {
      localPlayer = PlayerMapper.localPlayerFromDto(introductoryStateDto.getConnected, localControls)
      localStateSynchronizer.setLocalPlayer(localPlayer)
      playersContainer.add(localPlayer)

      val gameStateDto = introductoryStateDto.getGameState
      gameStateDto.getPlayers.asScala.map((playerDto: PlayerDto) =>
        PlayerMapper.localPlayerFromDto(playerDto, new NoopControls))
        .foreach(playersContainer.add)

      gameStateDto.getBullets.asScala.map((bulletDto: BulletDto) =>
        BulletMapper.fromDto(bulletDto, playersContainer))
        .foreach(bulletsContainer.add)

    })

    client.onOtherPlayerConnected((connectedDto) => {
      val connected = PlayerMapper.localPlayerFromDto(connectedDto, new NoopControls)
      playersContainer.add(connected)
    })

    client.onOtherPlayerDisconnected((uuidDto) => playersContainer.removeById(uuidDto.getUuid))
    client.onGameStateReceived(gameStateDto => {
      gameStateDto.getBullets.stream.forEach(bulletDto => {
        val bullet = bulletsContainer.getById(bulletDto.getId)
        if (!bullet.isPresent) bulletsContainer.add(BulletMapper.fromDto(bulletDto, playersContainer))
        else BulletMapper.updateByDto(bullet.get, bulletDto)

      })

      val existingBulletIds = gameStateDto.getBullets.asScala.map(_.getId).toList.asJava
      bulletsContainer
        .getAll
        .asScala
        .map(bullet => bullet.getId.toString)
        .filter(id => !existingBulletIds.contains(id))
        .toList
        .foreach(bulletsContainer.removeById)
    })

    localStateSynchronizer.updateAccordingToGameState(gameStateDto => {
      gameStateDto.getPlayers.stream.forEach(playerDto =>
        playersContainer
          .getById(playerDto.getId)
          .ifPresent(player => PlayerMapper.updateByDto(player, playerDto)))
    })

    localStateSynchronizer.supplyGameState(() => GameStateMapper.fromState(playersContainer, bulletsContainer))

    localStateSynchronizer.runGameLogic(this.runGameLogic)

    client.connect(PlayerDto(null, Randomize.fromList(Player.PossibleColors).toString, null))
  }


  override def render(delta: Float): Unit = {
    Gdx.gl.glClearColor(0, 0, 0, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

    if (!client.isConnected) return
    client.lockEventListeners()

    client.sendControls(ControlsMapper.mapToDto(localControls))
    runGameLogic(delta)
    localStateSynchronizer.recordState(delta, ControlsMapper.mapToDto(localControls))

    viewport.apply()
    shapeRenderer.setProjectionMatrix(viewport.getCamera.combined)
    shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
    playersRenderer.render(shapeRenderer)
    bulletsRenderer.render(shapeRenderer)
    shapeRenderer.end()
    client.unlockEventListeners()
  }

  override def resize(width: Int, height: Int): Unit = {
    viewport.update(width, height, true)
  }

  override def dispose(): Unit = {
    shapeRenderer.dispose
  }

  private def runGameLogic(delta: Float): Unit = {
    playersContainer.streamShips.forEach(arena.ensurePlacementWithinBounds)
    playersContainer.move(delta)
  }
}
