package isel.pdm.demos.tictactoe.infrastructure

import isel.pdm.demos.tictactoe.domain.game.LobbyEvent
import isel.pdm.demos.tictactoe.domain.game.PlayerInfo
import isel.pdm.demos.tictactoe.domain.game.RosterUpdated
import isel.pdm.demos.tictactoe.domain.user.UserInfo
import isel.pdm.demos.tictactoe.localTestPlayer
import isel.pdm.demos.tictactoe.otherTestPlayersInLobby
import isel.pdm.demos.tictactoe.utils.SuspendingCountDownLatch
import isel.pdm.demos.tictactoe.utils.SuspendingGate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LobbyFirebaseTests {

    @get:Rule
    val rule = PopulatedFirebaseLobbyRule()

    @Test(expected = IllegalStateException::class)
    fun enter_throws_when_already_entered() = runTest {
        // Arrange
        val sut = rule.lobby

        // Act
        sut.enter(localTestPlayer)
        sut.enter(localTestPlayer)
    }

    @Test(expected = IllegalStateException::class)
    fun leave_throws_when_not_entered() = runTest {
        // Arrange
        val sut = rule.lobby

        // Act
        sut.leave()
    }

    @Test
    fun enter_lobby_produces_a_roster_update_with_all_players() = runTest(dispatchTimeoutMs = 10000) {
        // Arrange
        val sut = rule.lobby

        // Act
        val gate = SuspendingGate()
        var collectedLobbyEvent: LobbyEvent? = null
        val collectJob = launch {
            sut.enter(localTestPlayer).collect {
                collectedLobbyEvent = it
                gate.open()
            }
        }

        // Lets wait for the first value to be emitted to the flow
        gate.await()
        collectJob.cancelAndJoin()

        // Assert
        val collectedRosterUpdated = collectedLobbyEvent as? RosterUpdated
        assertNotNull(
            "Expected RosterUpdated bot got $collectedLobbyEvent instead",
            collectedRosterUpdated != null
        )
        val playersInLobby = collectedRosterUpdated?.players ?: emptyList()
        val expectedInLobby = otherTestPlayersInLobby + localTestPlayer
        assertEquals(expectedInLobby.size, playersInLobby.size)
        assertTrue(playersInLobby.containsAll(expectedInLobby))
    }

    @Test
    fun updates_to_the_roster_are_emitted_to_the_flow() = runTest(dispatchTimeoutMs = 10000) {
        // Arrange
        val sut = rule.lobby

        // Act
        val gate = SuspendingCountDownLatch(initialCount = 2)
        var collectedLobbyEvent: LobbyEvent? = null
        val collectJob = launch {
            sut.enter(localTestPlayer).collect {
                collectedLobbyEvent = it
                gate.countDown()
            }
        }

        // Act
        val newPlayer = PlayerInfo(UserInfo("newPlayer", "newPlayer"))
        rule.addPlayerToLobby(newPlayer)

        // Lets wait for the values to be emitted to the flow
        gate.await()
        collectJob.cancelAndJoin()

        // Assert
        val collectedRosterUpdated = collectedLobbyEvent as? RosterUpdated
        assertNotNull(
            "Expected RosterUpdated bot got $collectedLobbyEvent instead",
            collectedRosterUpdated != null
        )
        val playersInLobby = collectedRosterUpdated?.players ?: emptyList()
        val expectedInLobby = otherTestPlayersInLobby + localTestPlayer + newPlayer
        assertEquals(expectedInLobby.size, playersInLobby.size)
        assertTrue(playersInLobby.containsAll(expectedInLobby))
    }
}