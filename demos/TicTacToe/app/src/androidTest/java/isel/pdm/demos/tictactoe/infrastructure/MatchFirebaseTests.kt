package isel.pdm.demos.tictactoe.infrastructure

import isel.pdm.demos.tictactoe.TicTacToeTestApplication
import isel.pdm.demos.tictactoe.domain.game.lobby.Challenge
import isel.pdm.demos.tictactoe.domain.game.lobby.firstToMove
import isel.pdm.demos.tictactoe.domain.game.lobby.getMarkerFor
import isel.pdm.demos.tictactoe.domain.game.lobby.secondToMove
import isel.pdm.demos.tictactoe.domain.game.play.Board
import isel.pdm.demos.tictactoe.domain.game.play.Coordinate
import isel.pdm.demos.tictactoe.domain.game.play.Game
import isel.pdm.demos.tictactoe.domain.game.play.MatchEvent
import isel.pdm.demos.tictactoe.domain.game.play.forfeit
import isel.pdm.demos.tictactoe.domain.game.play.makeMove
import isel.pdm.demos.tictactoe.domain.game.play.startGame
import isel.pdm.demos.tictactoe.utils.SuspendingGate
import isel.pdm.demos.tictactoe.utils.awaitAndThenAssert
import isel.pdm.demos.tictactoe.utils.xAssertIs
import isel.pdm.demos.tictactoe.utils.xAssertNotNull
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

/**
 * This test suite requires the Firebase's Firestore emulator to be running.
 */
class MatchFirebaseTests {

    @get:Rule
    val rule = CleanupFirebaseMatchRule()

    @Test
    fun localPlayerMarker_returns_null_when_match_not_started() {
        // Arrange
        val sut = rule.match

        // Act
        val result = sut.localPlayerMarker

        // Assert
        assertNull(result)
    }

    @Test
    fun started_event_is_produced_when_game_is_created() = runBlocking {
        // Arrange
        val sut = rule.match
        val challenge = rule.locallyInitiatedChallenge
        val localPlayer = challenge.challenger
        val expectedEventGate = SuspendingGate()

        // Act
        var collectedEvent: MatchEvent.Started? = null
        val collectJob = launch {
            sut.start(localPlayer, challenge).collect {
                if (it is MatchEvent.Started) {
                    collectedEvent = it
                    expectedEventGate.open()
                }
            }
        }
        addNewGame(rule.testApp, challenge)

        expectedEventGate.awaitAndThenAssert(10000) {
            collectJob.cancelAndJoin()
            // Assert
            val startedEvent = collectedEvent
            xAssertNotNull(startedEvent)
            assertEquals(Board.EMPTY, startedEvent.game.board)
            assertEquals(challenge.getMarkerFor(challenge.firstToMove), startedEvent.game.turn)
        }
    }

    @Test
    fun start_publishes_match_when_local_player_is_challenged() = runBlocking {
        // Arrange
        val sut = rule.match
        val challenge = rule.remoteInitiatedChallenge
        val localPlayer = challenge.challenged
        val expectedEventGate = SuspendingGate()

        // Act
        var collectedEvent: MatchEvent.Started? = null
        val collectJob = launch {
            sut.start(localPlayer, challenge).collect {
                if (it is MatchEvent.Started) {
                    collectedEvent = it
                    expectedEventGate.open()
                }
            }
        }

        expectedEventGate.awaitAndThenAssert(5000) {
            collectJob.cancelAndJoin()
            // Assert
            val startedEvent = collectedEvent
            xAssertNotNull(startedEvent)
            assertEquals(Board.EMPTY, startedEvent.game.board)
            assertEquals(challenge.getMarkerFor(challenge.firstToMove), startedEvent.game.turn)
        }
    }

    @Test
    fun start_on_an_already_started_match_throws(): Unit = runBlocking {
        // Arrange
        val sut = rule.match
        val challenge = rule.locallyInitiatedChallenge
        val localPlayer = challenge.challenger
        sut.start(localPlayer, challenge)

        // Act & Assert
        assertThrows(IllegalStateException::class.java) {
            sut.start(localPlayer, challenge)
        }
    }

    @Test
    fun localPlayerMarker_returns_the_local_player_marker_when_match_started() = runBlocking {
        // Arrange
        val sut = rule.match
        val challenge = rule.locallyInitiatedChallenge
        val localPlayer = challenge.challenger
        val expectedEventGate = SuspendingGate()
        val expectedLocalPlayerMarker = challenge.getMarkerFor(localPlayer)

        // Act
        val collectJob = launch {
            sut.start(localPlayer, challenge).collect {
                if (it is MatchEvent.Started) {
                    expectedEventGate.open()
                }
            }
        }

        addNewGame(rule.testApp, challenge)

        expectedEventGate.awaitAndThenAssert(5000) {
            collectJob.cancelAndJoin()
            // Assert
            assertEquals(expectedLocalPlayerMarker, sut.localPlayerMarker)
        }
    }

    @Test
    fun moveMade_event_is_produced_when_remote_player_makes_a_move() = runBlocking {
        // Arrange
        val sut = rule.match
        val challenge = rule.remoteInitiatedChallenge
        val localPlayer = challenge.challenged
        val expectedEventGate = SuspendingGate()

        var collectedEvent: MatchEvent.MoveMade? = null
        val collectJob = launch {
            sut.start(localPlayer, challenge).collect {
                if (it is MatchEvent.MoveMade) {
                    collectedEvent = it
                    expectedEventGate.open()
                }
            }
        }

        // Act
        val (gameId, game) = addNewGame(rule.testApp, challenge)
        val playedAt = Coordinate(0, 0)
        updateGame(rule.testApp, gameId, game.makeMove(at = playedAt))

        expectedEventGate.awaitAndThenAssert(5000) {
            collectJob.cancelAndJoin()
            // Assert
            val moveMadeEvent = collectedEvent
            xAssertNotNull(moveMadeEvent)
            assertEquals(challenge.getMarkerFor(challenge.firstToMove), moveMadeEvent.game.board[playedAt])
            assertEquals(challenge.getMarkerFor(challenge.secondToMove), moveMadeEvent.game.turn)
        }
    }

    @Test
    fun moveMade_publishes_match_when_local_player_makes_a_move() = runBlocking {
        // Arrange
        val sut = rule.match
        val challenge = rule.locallyInitiatedChallenge
        val localPlayer = challenge.challenger
        val expectedEventGate = SuspendingGate()

        var collectedEvent: MatchEvent.MoveMade? = null
        val collectJob = launch {
            sut.start(localPlayer, challenge).collect {
                if (it is MatchEvent.MoveMade) {
                    collectedEvent = it
                    expectedEventGate.open()
                }
            }
        }
        addNewGame(rule.testApp, challenge)

        // Act
        val playedAt = Coordinate(0, 0)
        sut.makeMove(at = playedAt)

        expectedEventGate.awaitAndThenAssert(5000) {
            collectJob.cancelAndJoin()
            // Assert
            val moveMadeEvent = collectedEvent
            xAssertNotNull(moveMadeEvent)
            assertEquals(challenge.getMarkerFor(challenge.firstToMove), moveMadeEvent.game.board[playedAt])
            assertEquals(challenge.getMarkerFor(challenge.secondToMove), moveMadeEvent.game.turn)
        }
    }

    @Test(expected = IllegalStateException::class)
    fun makeMove_when_its_not_the_local_player_turn_throws() = runBlocking {
        // Arrange
        val sut = rule.match
        val challenge = rule.locallyInitiatedChallenge
        val localPlayer = challenge.challenger
        val expectedEventGate = SuspendingGate()

        val collectJob = launch {
            sut.start(localPlayer, challenge).collect {
                if (it is MatchEvent.MoveMade) {
                    expectedEventGate.open()
                }
            }
        }
        addNewGame(rule.testApp, challenge)

        val playedAt = Coordinate(0, 0)
        sut.makeMove(at = playedAt)
        withTimeout(5000) { expectedEventGate.await() }
        collectJob.cancelAndJoin()

        // Act & Assert
        sut.makeMove(at = Coordinate(1, 1))
    }

    @Test
    fun makeMove_when_its_local_player_turn_succeeds() = runBlocking {
        // Arrange
        val sut = rule.match
        val challenge = rule.locallyInitiatedChallenge
        val localPlayer = challenge.challenged

        val startedEventGate = SuspendingGate()
        val expectedEventGate = SuspendingGate()
        var startedEvent: MatchEvent.Started? = null

        val collectJob = launch {
            sut.start(localPlayer, challenge).collect {
                if (it is MatchEvent.Started) {
                    startedEvent = it
                    startedEventGate.open()
                }
                if (it is MatchEvent.MoveMade) {
                    expectedEventGate.open()
                }
            }
        }

        withTimeout(5000) { startedEventGate.await() }

        // Simulate remote player move
        val playedAt = Coordinate(0, 0)
        val gameId = challenge.challenger.id.toString()
        val game = startedEvent?.game?.makeMove(at = Coordinate(0, 0))
        xAssertNotNull(game)
        updateGame(rule.testApp, gameId, game)

        // Act & Assert
        expectedEventGate.awaitAndThenAssert(5000) {
            collectJob.cancelAndJoin()
            sut.makeMove(at = Coordinate(1, 1))
        }
    }

    @Test
    fun forfeit_event_is_produced_when_remote_player_forfeits() = runBlocking {
        // Arrange
        val sut = rule.match
        val challenge = rule.locallyInitiatedChallenge
        val localPlayer = challenge.challenger
        val remotePlayer = challenge.challenged
        val expectedEventGate = SuspendingGate()

        var collectedEvent: MatchEvent.Ended? = null
        val collectJob = launch {
            sut.start(localPlayer, challenge).collect {
                if (it is MatchEvent.Ended) {
                    collectedEvent = it
                    expectedEventGate.open()
                }
            }
        }
        val (gameId, game) = addNewGame(rule.testApp, challenge)

        // Act
        updateGame(rule.testApp, gameId, game.forfeit(challenge.getMarkerFor(remotePlayer)))

        expectedEventGate.awaitAndThenAssert(5000) {
            collectJob.cancelAndJoin()
            // Assert
            xAssertNotNull(collectedEvent)
            assertEquals(challenge.getMarkerFor(localPlayer), collectedEvent?.winner)
            xAssertIs<Game.HasWinner>(collectedEvent?.game)
                .also { assertTrue(it.wasForfeited) }
        }
    }

    @Test
    fun forfeit_publishes_match_when_local_player_forfeits() = runBlocking {
        // Arrange
        val sut = rule.match
        val challenge = rule.remoteInitiatedChallenge
        val localPlayer = challenge.challenged
        val expectedEventGate = SuspendingGate()

        var collectedEvent: MatchEvent.Ended? = null
        val collectJob = launch {
            sut.start(localPlayer, challenge).collect {
                if (it is MatchEvent.Ended) {
                    collectedEvent = it
                    expectedEventGate.open()
                }
            }
        }
        addNewGame(rule.testApp, challenge)

        // Act
        sut.forfeit()

        expectedEventGate.awaitAndThenAssert(5000) {
            collectJob.cancelAndJoin()
            // Assert
            xAssertNotNull(collectedEvent)
            assertEquals(challenge.getMarkerFor(challenge.challenger), collectedEvent?.winner)
            xAssertIs<Game.HasWinner>(collectedEvent?.game)
                .also { assertTrue(it.wasForfeited) }
        }
    }

    @Test
    fun close_on_a_not_started_match_does_nothing() = runBlocking {
        // Arrange
        val sut = rule.match

        // Act
        sut.close()
    }

    @Test
    fun close_on_an_going_game_forfeits_it_for_the_local_player() = runBlocking {
        // Arrange
        val sut = rule.match
        val challenge = rule.locallyInitiatedChallenge
        val localPlayer = challenge.challenger
        val remotePlayer = challenge.challenged
        val expectedEventGate = SuspendingGate()

        var collectedEvent: MatchEvent.Ended? = null
        val collectJob = launch {
            sut.start(localPlayer, challenge).collect {
                if (it is MatchEvent.Ended) {
                    collectedEvent = it
                    expectedEventGate.open()
                }
            }
        }
        addNewGame(rule.testApp, challenge)

        // Act
        sut.close()

        expectedEventGate.awaitAndThenAssert(5000) {
            collectJob.cancelAndJoin()
            // Assert
            xAssertNotNull(collectedEvent)
            assertEquals(challenge.getMarkerFor(remotePlayer), collectedEvent?.winner)
            xAssertIs<Game.HasWinner>(collectedEvent?.game)
                .also { assertTrue(it.wasForfeited) }
        }
    }

    @Test
    fun flow_returned_by_start_is_closed_when_game_document_is_deleted() = runBlocking {
        // Arrange
        val sut = rule.match
        val challenge = rule.locallyInitiatedChallenge
        val localPlayer = challenge.challenger
        val flowClosedGate = SuspendingGate()

        launch {
            sut.start(localPlayer, challenge).collect { }
            flowClosedGate.open()
        }
        addNewGame(rule.testApp, challenge)

        // Act
        sut.close()

        // Assert
        withTimeout(5000) { flowClosedGate.await() }
    }
}

/**
 * Adds a new game to the ongoing games collection to the Firestore emulator.
 */
suspend fun addNewGame(testApp: TicTacToeTestApplication, challenge: Challenge): Pair<String, Game> {
    val game = startGame()
    val gameId = challenge.firstToMove.id.toString()
    testApp.emulatedFirestoreDb
        .collection(ONGOING)
        .document(gameId)
        .set(game.toDocumentContent())
        .await()

    return Pair(gameId, game)
}

/**
 * Updates an ongoing game to the Firestore emulator.
 */
suspend fun updateGame(testApp: TicTacToeTestApplication, gameId: String, game: Game) {
    testApp.emulatedFirestoreDb
        .collection(ONGOING)
        .document(gameId)
        .update(game.toDocumentContent())
        .await()
}
