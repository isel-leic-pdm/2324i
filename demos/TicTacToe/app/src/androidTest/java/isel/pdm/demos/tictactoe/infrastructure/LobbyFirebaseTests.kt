package isel.pdm.demos.tictactoe.infrastructure

import isel.pdm.demos.tictactoe.domain.game.lobby.LobbyEvent
import isel.pdm.demos.tictactoe.domain.game.lobby.PlayerInfo
import isel.pdm.demos.tictactoe.domain.user.UserInfo
import isel.pdm.demos.tictactoe.utils.SuspendingGate
import isel.pdm.demos.tictactoe.utils.awaitAndThenAssert
import isel.pdm.demos.tictactoe.utils.xAssertIs
import isel.pdm.demos.tictactoe.utils.xAssertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

/**
 * This test suite requires the Firebase's Firestore emulator to be running.
 *
 * These tests are designed to not be dependent of the specific sequence of events
 * produced by the Firebase's Firestore implementation. This means that the tests
 * wait for the expected events to be produced by the flow and, once they are,
 * they perform the assertions.
 *
 */
@OptIn(ExperimentalCoroutinesApi::class)
class LobbyFirebaseTests {

    @get:Rule
    val rule = PopulatedFirebaseLobbyRule()

    @Test(expected = IllegalStateException::class)
    fun enter_throws_when_already_entered(): Unit = runBlocking {
        // Arrange
        val sut = rule.lobby

        // Act
        sut.enter(localTestPlayer)
        sut.enter(localTestPlayer)
    }

    @Test(expected = IllegalStateException::class)
    fun leave_throws_when_not_entered() = runBlocking {
        // Arrange
        val sut = rule.lobby

        // Act
        sut.leave()
    }

    @Test
    fun enter_lobby_produces_a_roster_update_that_includes_the_player() = runBlocking {
        // Arrange
        val sut = rule.lobby
        val expectedInLobby = otherTestPlayersInLobby + localTestPlayer
        val expectedEventGate = SuspendingGate()

        fun isExpectedLobbyEvent(evt: LobbyEvent) =
            evt is LobbyEvent.RosterUpdated && evt.players.containsAll(expectedInLobby)

        // Act
        var collectedEvent: LobbyEvent? = null
        val collectJob = launch {
            sut.enter(localTestPlayer).collect {
                if (isExpectedLobbyEvent(it)) {
                    collectedEvent = it
                    expectedEventGate.open()
                }
            }
        }

        expectedEventGate.awaitAndThenAssert(10000) {
            collectJob.cancelAndJoin()
            // Assert
            val rosterUpdated = xAssertIs<LobbyEvent.RosterUpdated>(collectedEvent) {
                "Expected RosterUpdated bot got $collectedEvent instead"
            }
            assertEquals(expectedInLobby.size, rosterUpdated.players.size)
            assertTrue(rosterUpdated.players.containsAll(expectedInLobby))
        }
    }

    @Test
    fun updates_to_the_roster_are_emitted_to_the_flow() = runBlocking {
        // Arrange
        val sut = rule.lobby
        val newPlayer = PlayerInfo(UserInfo("newPlayer", "newPlayer"))
        val gateLobbyEntered = SuspendingGate()
        val expectedEventGate = SuspendingGate()

        fun isExpectedLobbyEvent(evt: LobbyEvent) =
            evt is LobbyEvent.RosterUpdated && evt.players.contains(newPlayer)

        var collectedLobbyEvent: LobbyEvent? = null
        val collectJob = launch {
            // The local player enters the lobby
            val lobbyFlow = sut.enter(localTestPlayer)
            gateLobbyEntered.open()
            lobbyFlow.collect {
                if (isExpectedLobbyEvent(it)) {
                    collectedLobbyEvent = it
                }
            }
        }

        // Act: a new remote player enters the lobby
        rule.addPlayerToLobby(newPlayer)

        expectedEventGate.awaitAndThenAssert(10000) {
            collectJob.cancelAndJoin()
            // Assert
            val rosterUpdated = xAssertIs<LobbyEvent.RosterUpdated>(collectedLobbyEvent) {
                "Expected RosterUpdated bot got $collectedLobbyEvent instead"
            }
            val expectedInLobby = otherTestPlayersInLobby + localTestPlayer + newPlayer
            assertEquals(expectedInLobby.size, rosterUpdated.players.size)
            assertTrue(rosterUpdated.players.containsAll(expectedInLobby))
        }
    }

    @Test
    fun leave_lobby_removes_the_player_from_the_roster_and_closes_the_flow() = runBlocking {
        // Arrange
        val sut = rule.lobby
        val gateLobbyEntered = SuspendingGate()
        val gateFlowClosed = SuspendingGate()

        fun hasEntered(evt: LobbyEvent) =
            evt is LobbyEvent.RosterUpdated && evt.players.containsAll(otherTestPlayersInLobby + localTestPlayer)

        var lastCollectedLobbyEvent: LobbyEvent? = null
        val collectJob = launch {
            sut.enter(localTestPlayer).collect {
                lastCollectedLobbyEvent = it
                if (hasEntered(it)) {
                    gateLobbyEntered.open()
                }
            }
            // The flow was closed
            gateFlowClosed.open()
        }

        withTimeout(10000) { gateLobbyEntered.await() }

        // Act: the local player leaves the lobby
        sut.leave()

        gateFlowClosed.awaitAndThenAssert(10000) {
            collectJob.cancelAndJoin()
            // Assert
            val rosterUpdated = xAssertIs<LobbyEvent.RosterUpdated>(lastCollectedLobbyEvent) {
                "Expected RosterUpdated bot got $lastCollectedLobbyEvent instead"
            }
            val expectedInLobby = otherTestPlayersInLobby
            assertEquals(expectedInLobby.size, rosterUpdated.players.size)
            assertTrue(rosterUpdated.players.containsAll(expectedInLobby))
        }
    }

    @Test
    fun issueChallenge_updates_challenged_player_document() = runBlocking {
        // Arrange
        val sut = rule.lobby
        val challengedPlayer = otherTestPlayersInLobby.first()
        val gateLobbyEntered = SuspendingGate()

        fun hasEntered(evt: LobbyEvent) =
            evt is LobbyEvent.RosterUpdated && evt.players.containsAll(otherTestPlayersInLobby + localTestPlayer)

        val collectJob = launch {
            sut.enter(localTestPlayer).collect {
                if (hasEntered(it)) {
                    gateLobbyEntered.open()
                }
            }
        }

        withTimeout(10000) { gateLobbyEntered.await() }

        // Act: the local player issues a challenge to the first player in the lobby
        sut.issueChallenge(challengedPlayer)

        // Assert
        collectJob.cancelAndJoin()
        val challenge = rule.getChallengeInfo(challengedPlayer)
        xAssertNotNull(challenge)
        assertEquals(localTestPlayer, challenge.challenger)
        assertEquals(challengedPlayer, challenge.challenged)
    }

    @Test
    fun updating_local_player_doc_with_challenger_info_issues_ChallengeReceived() = runBlocking {
        // Arrange
        val sut = rule.lobby
        val challengerPlayer = otherTestPlayersInLobby.first()
        val challengeReceivedGate = SuspendingGate()
        val gateLobbyEntered = SuspendingGate()

        var collectedLobbyEvent: LobbyEvent? = null
        val collectJob = launch {
            sut.enter(localTestPlayer).collect {
                when (it) {
                    is LobbyEvent.RosterUpdated -> {
                        if (it.players.containsAll(otherTestPlayersInLobby + localTestPlayer)) {
                            gateLobbyEntered.open()
                        }
                    }
                    is LobbyEvent.ChallengeReceived -> {
                        collectedLobbyEvent = it
                        challengeReceivedGate.open()
                    }
                }
            }
        }

       // Act: updated local player doc with  the  challenger info as soon as the local player enters the lobby
        withTimeout(10000) { gateLobbyEntered.await() }
        rule.updateChallengerInfo(localTestPlayer, challengerPlayer)

        // Assert
        challengeReceivedGate.awaitAndThenAssert(10000) {
            collectJob.cancelAndJoin()
            val challengeReceived = xAssertIs<LobbyEvent.ChallengeReceived>(collectedLobbyEvent) {
                "Expected ChallengeReceived bot got $collectedLobbyEvent instead"
            }
            assertEquals(challengerPlayer, challengeReceived.challenge.challenger)
            assertEquals(localTestPlayer, challengeReceived.challenge.challenged)
        }
    }
}
