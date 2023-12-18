package isel.pdm.demos.tictactoe.ui.game.play

import io.mockk.coEvery
import io.mockk.mockk
import isel.pdm.demos.tictactoe.domain.game.lobby.Challenge
import isel.pdm.demos.tictactoe.domain.game.lobby.PlayerInfo
import isel.pdm.demos.tictactoe.domain.game.lobby.firstToMove
import isel.pdm.demos.tictactoe.domain.game.lobby.getMarkerFor
import isel.pdm.demos.tictactoe.domain.game.play.Coordinate
import isel.pdm.demos.tictactoe.domain.game.play.Game
import isel.pdm.demos.tictactoe.domain.game.play.Match
import isel.pdm.demos.tictactoe.domain.game.play.MatchEvent
import isel.pdm.demos.tictactoe.domain.game.play.forfeit
import isel.pdm.demos.tictactoe.domain.game.play.makeMove
import isel.pdm.demos.tictactoe.domain.game.play.startGame
import isel.pdm.demos.tictactoe.domain.user.UserInfo
import isel.pdm.demos.tictactoe.utils.MockMainDispatcherRule
import isel.pdm.demos.tictactoe.utils.SuspendingGate
import isel.pdm.demos.tictactoe.utils.awaitAndThenAssert
import isel.pdm.demos.tictactoe.utils.xAssertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GamePlayScreenViewModelTests {

    @get:Rule
    val rule = MockMainDispatcherRule()

    private val testMatch: Match = mockk<Match>(relaxed = true) {
        val matchEndsGate = SuspendingGate()
        val makeMoveGate = SuspendingGate()
        coEvery { start(any(), any()) } returns flow {
            val game = startGame(locallyInitiatedChallenge.getMarkerFor(locallyInitiatedChallenge.firstToMove))
            emit(MatchEvent.Started(game))

            makeMoveGate.await()
            val gameAfterMove = game.makeMove(at = Coordinate(0, 0))
            emit(MatchEvent.MoveMade(game = gameAfterMove as Game.Ongoing))

            matchEndsGate.await()
            emit(MatchEvent.Ended(gameAfterMove.forfeit(locallyInitiatedChallenge.getMarkerFor(localPlayer))))
        }
        coEvery { makeMove(any()) } coAnswers { makeMoveGate.open() }
        coEvery { forfeit() } coAnswers { makeMoveGate.open(); matchEndsGate.open() }
    }

    @Test
    fun startMatch_on_idle_state_places_screen_in_starting_state(): Unit = runTest {
        // Arrange
        val expectedStateGate = SuspendingGate()
        val mockMatch = mockk<Match>(relaxed = true) {
            coEvery { start(any(), any()) } returns flow { }
        }
        val sut = GamePlayScreenViewModel(mockMatch)

        // Act
        sut.startMatch(localPlayer, locallyInitiatedChallenge)
        var collectedState: GamePlayScreenState.Starting? = null
        val collectJob = launch {
            sut.screenState.collect {
                if (it is GamePlayScreenState.Starting) {
                    collectedState = it
                    expectedStateGate.open()
                }
            }
        }

        // Assert
        expectedStateGate.awaitAndThenAssert(5000) {
            collectJob.cancelAndJoin()
            val startingState = collectedState
            xAssertNotNull(startingState)
            assertEquals(locallyInitiatedChallenge, startingState.challenge)
        }
    }

    @Test(expected = IllegalStateException::class)
    fun startMatch_on_non_idle_state_throws() = runTest {
        // Arrange
        val sut = GamePlayScreenViewModel(testMatch)
        sut.startMatch(localPlayer, locallyInitiatedChallenge)

        // Act
        sut.startMatch(localPlayer, locallyInitiatedChallenge)
    }

    @Test
    fun screen_is_in_started_state_when_match_starts(): Unit = runTest {
        // Arrange
        val expectedStateGate = SuspendingGate()
        val sut = GamePlayScreenViewModel(testMatch)

        // Act
        sut.startMatch(localPlayer, remotelyInitiatedChallenge)
        var collectedState: GamePlayScreenState.Started? = null
        val collectJob = launch {
            sut.screenState.collect {
                if (it is GamePlayScreenState.Started) {
                    collectedState = it
                    expectedStateGate.open()
                }
            }
        }

        // Assert
        expectedStateGate.awaitAndThenAssert(5000) {
            collectJob.cancelAndJoin()
            val startedState = collectedState
            xAssertNotNull(startedState)
            val expectedLocalMarker = remotelyInitiatedChallenge.getMarkerFor(localPlayer)
            assertEquals(expectedLocalMarker, startedState.localPlayerMarker)
        }
    }

    @Test
    fun forfeit_on_started_match_finishes_it() = runTest {
        // Arrange
        val sut = GamePlayScreenViewModel(testMatch)
        sut.startMatch(localPlayer, remotelyInitiatedChallenge)
        val matchStartedGate = SuspendingGate()
        val expectedStateGate = SuspendingGate()
        var collectedState: GamePlayScreenState.Finished? = null
        val collectJob = launch {
            sut.screenState.collect {
                if (it is GamePlayScreenState.Started) {
                    matchStartedGate.open()
                }
                if (it is GamePlayScreenState.Finished) {
                    collectedState = it
                    expectedStateGate.open()
                }
            }
        }
        matchStartedGate.await()

        // Act
        sut.forfeit()
        expectedStateGate.await()

        // Assert
        collectJob.cancelAndJoin()
        val finishedState = collectedState
        xAssertNotNull(finishedState)
        val expectedLocalMarker = remotelyInitiatedChallenge.getMarkerFor(localPlayer)
        assertEquals(expectedLocalMarker, finishedState.localPlayerMarker)
    }

    @Test(expected = IllegalStateException::class)
    fun forfeit_on_non_started_match_throws() = runTest {
        // Arrange
        val sut = GamePlayScreenViewModel(testMatch)

        // Act
        sut.forfeit()
    }

    @Test(expected = IllegalStateException::class)
    fun makeMove_on_nonStarted_match_throws() = runTest {
        // Arrange
        val sut = GamePlayScreenViewModel(testMatch)

        // Act
        sut.makeMove(at = Coordinate(0, 0))
    }

    @Test
    fun makeMove_on_started_match_succeeds() = runTest {
        // Arrange
        val sut = GamePlayScreenViewModel(testMatch)
        val startedEventGate = SuspendingGate()
        val expectedStateGate = SuspendingGate()
        val moveAt = Coordinate(0, 0)

        var collectedState: GamePlayScreenState.Started? = null
        val collectJob = launch {
            sut.screenState.collect {
                if (it is GamePlayScreenState.Started) {
                    val lastCollected = collectedState
                    collectedState = it
                    if (lastCollected == null) startedEventGate.open()
                    else expectedStateGate.open()
                }
            }
        }

        sut.startMatch(localPlayer, locallyInitiatedChallenge)
        withTimeout(3000) { startedEventGate.await() }

        // Act
        sut.makeMove(at = moveAt)

        // Assert
        expectedStateGate.awaitAndThenAssert(3000) {
            collectJob.cancelAndJoin()
            val startedState = collectedState
            xAssertNotNull(startedState)
            assertNotNull(startedState.game.board[moveAt])
        }
    }
}

private val localPlayer = PlayerInfo(info = UserInfo("localPlayer", "localPlayerMotto"))
private val remotePlayer = PlayerInfo(info = UserInfo("remotePlayer", "remotePlayerMotto"))

private val locallyInitiatedChallenge = Challenge(challenger = localPlayer, challenged = remotePlayer)
private val remotelyInitiatedChallenge = Challenge(challenger = remotePlayer, challenged = localPlayer)

