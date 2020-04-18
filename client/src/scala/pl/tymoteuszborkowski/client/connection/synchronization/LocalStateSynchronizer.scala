package pl.tymoteuszborkowski.client.connection.synchronization

import java.util
import java.util.Collections.synchronizedList
import java.util.function.{Consumer, Supplier}

import pl.tymoteuszborkowski.controls.RemoteControls
import pl.tymoteuszborkowski.domain.Player
import pl.tymoteuszborkowski.dto.mapper.ControlsMapper
import pl.tymoteuszborkowski.dto.{ControlsDto, GameStateDto, IndexedDto}


class LocalStateSynchronizer(
                              recordedStates: util.List[LocalState] = synchronizedList(new util.ArrayList[LocalState]),
                              synchronizationControls: RemoteControls = new RemoteControls) {


  private var currentIndex = 0L
  private var gameStateUpdater: Consumer[GameStateDto] = _
  private var gameLogicRunner: Consumer[Float] = _
  private var gameStateSupplier: Supplier[GameStateDto] = _
  private var localPlayer: Player = _

  def updateAccordingToGameState(updater: Consumer[GameStateDto]): Unit = {
    gameStateUpdater = updater
  }

  def runGameLogic(runner: Consumer[Float]): Unit = {
    gameLogicRunner = runner
  }

  def setLocalPlayer(localPlayer: Player): Unit = {
    this.localPlayer = localPlayer
  }

  def supplyGameState(supplier: Supplier[GameStateDto]): Unit = {
    gameStateSupplier = supplier
  }

  def getCurrentIndex: Long = currentIndex

  def recordState(delta: Float, controlsDto: ControlsDto): Unit = {
    recordedStates.add(new LocalState(currentIndex, delta, controlsDto, gameStateSupplier.get))
    currentIndex += 1
  }

  def synchronize(latestState: IndexedDto[GameStateDto]): Unit = {
    discardSnapshotsUntil(latestState.getIndex)
    if (recordedStates.size == 0) return
    val latestLocalState = recordedStates.get(0)
    if (latestLocalState.getIndex != latestState.getIndex) return
    if (latestLocalState.gameStateMatches(latestState.getDto)) return
    returnToLatestServerState(latestState)
    rerunGameLogic()
  }

  private def rerunGameLogic(): Unit = {
    val playerOriginalControls = localPlayer.getControls
    localPlayer.setControls(synchronizationControls)
    var i = 1
    while (i < recordedStates.size) {
      val localState = recordedStates.get(i)
      ControlsMapper.setRemoteControlsByDto(localState.getControlsDto, synchronizationControls)
      gameLogicRunner.accept(localState.getDelta)
      recordedStates.set(i, updateState(localState))

      i += 1
    }
    localPlayer.setControls(playerOriginalControls)
  }

  private def returnToLatestServerState(latestState: IndexedDto[GameStateDto]): Unit = {
    gameStateUpdater.accept(latestState.getDto)
    recordedStates.set(0, updateState(recordedStates.get(0)))
  }

  private def discardSnapshotsUntil(boundaryIndex: Long): Unit = {
    recordedStates.removeIf((localState) => localState.getIndex < boundaryIndex)
  }

  private def updateState(oldState: LocalState) =
    new LocalState(oldState.getIndex, oldState.getDelta, oldState.getControlsDto, gameStateSupplier.get)

}
