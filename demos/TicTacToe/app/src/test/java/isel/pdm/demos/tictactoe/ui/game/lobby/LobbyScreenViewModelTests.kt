package isel.pdm.demos.tictactoe.ui.game.lobby

import android.util.Log
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import isel.pdm.demos.tictactoe.domain.game.lobby.Challenge
import isel.pdm.demos.tictactoe.domain.game.lobby.Lobby
import isel.pdm.demos.tictactoe.domain.game.lobby.LobbyEvent
import isel.pdm.demos.tictactoe.domain.game.lobby.PlayerInfo
import isel.pdm.demos.tictactoe.domain.user.UserInfo
import isel.pdm.demos.tictactoe.utils.MockMainDispatcherRule
import isel.pdm.demos.tictactoe.utils.SuspendingGate
import isel.pdm.demos.tictactoe.utils.xAssertIs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LobbyScreenViewModelTests {

    init {
        mockkStatic(Log::class)
        every { Log.v(any(), any()) } returns 0
        every { Log.d(any(), any()) } returns 0
    }

    @get:Rule
    val rule = MockMainDispatcherRule()

    private val testLobby: Lobby = mockk(relaxed = true) {
        val localPlayer = slot<PlayerInfo>()
        coEvery { enter(capture(localPlayer)) } returns flow {
            emit(LobbyEvent.RosterUpdated(
                buildList {
                    add(localPlayer.captured)
                    addAll(otherTestPlayersInLobby)
                }
            ))

            delay(1000)
            val challenge = Challenge(
                challenger = otherTestPlayersInLobby.first(),
                challenged = localPlayer.captured
            )
            emit(LobbyEvent.ChallengeReceived(challenge))
        }

        val opponent = slot<PlayerInfo>()
        coEvery { issueChallenge(capture(opponent)) } answers {
            Challenge(
                challenger = localPlayer.captured,
                challenged = opponent.captured
            )
        }

        coEvery { leave() } returns Unit
    }

    @Test
    fun enterLobby_produces_InsideLobby_with_the_local_player_excluded_from_the_list() = runTest {
        // Arrange
        val sut = LobbyScreenViewModel(testLobby, localTestUserInfo)
        val insideLobbyGate = SuspendingGate()
        var collectedState: LobbyScreenState.InsideLobby? = null

        // Act
        val collectJob = launch {
            sut.enterLobby()
            sut.screenState.collect {
                if (it is LobbyScreenState.InsideLobby) {
                    collectedState = it
                    insideLobbyGate.open()
                }
            }
        }

        insideLobbyGate.await()
        collectJob.cancelAndJoin()

        // Assert
        val state = xAssertIs<LobbyScreenState.InsideLobby>(collectedState)
        assertEquals(otherTestPlayersInLobby.size, state.otherPlayers.size)
        assertTrue(state.otherPlayers.containsAll(otherTestPlayersInLobby))
    }

    @Test
    fun enterLobby_produces_LobbyAccessError_when_lobby_enter_fails() = runTest {
        // Arrange
        val expectedException = Exception("test exception")
        val failingTestLobby = mockk<Lobby> {
            coEvery { enter(any()) } returns callbackFlow { close(expectedException) }
        }
        val sut = LobbyScreenViewModel(failingTestLobby, localTestUserInfo)
        val errorGate = SuspendingGate()
        var collectedState: LobbyScreenState? = null

        val collectJob = launch {
            sut.screenState.collect {
                collectedState = it
                if (it is LobbyScreenState.LobbyAccessError)
                    errorGate.open()
            }
        }

        // Act
        sut.enterLobby()

        errorGate.await()
        collectJob.cancelAndJoin()

        // Assert
        val state = xAssertIs<LobbyScreenState.LobbyAccessError>(collectedState)
        assertEquals(expectedException.message, state.cause.message)
    }

    @Test
    fun sendChallenge_produces_SentChallenge() = runTest {
        // Arrange
        val sut = LobbyScreenViewModel(testLobby, localTestUserInfo)
        val insideLobbyGate = SuspendingGate()
        val sentChallengeGate = SuspendingGate()
        var collectedState: LobbyScreenState? = null

        sut.enterLobby()
        val collectJob = launch {
            sut.screenState.collect {
                collectedState = it
                when (it) {
                    is LobbyScreenState.InsideLobby -> insideLobbyGate.open()
                    is LobbyScreenState.SentChallenge -> sentChallengeGate.open()
                    else -> {}
                }
            }
        }

        withTimeout(10000) { insideLobbyGate.await() }

        // Act
        val opponent = otherTestPlayersInLobby.first()
        sut.sendChallenge(opponent)

        // Assert
        sentChallengeGate.awaitAndThenAssert(10000) {
            collectJob.cancelAndJoin()
            val state = xAssertIs<LobbyScreenState.SentChallenge>(collectedState)
            assertEquals(localTestUserInfo, state.localPlayer.info)
            assertEquals(opponent, state.challenge.challenged)
        }
    }

    @Test
    fun received_challenge_produces_IncomingChallenge() = runTest {
        // Arrange
        val sut = LobbyScreenViewModel(testLobby, localTestUserInfo)
        val insideLobbyGate = SuspendingGate()
        val incomingChallengeGate = SuspendingGate()
        var collectedState: LobbyScreenState? = null

        sut.enterLobby()
        val collectJob = launch {
            sut.screenState.collect {
                collectedState = it
                when (it) {
                    is LobbyScreenState.InsideLobby -> insideLobbyGate.open()
                    is LobbyScreenState.IncomingChallenge -> incomingChallengeGate.open()
                    else -> {}
                }
            }
        }

        withTimeout(10000) { insideLobbyGate.await() }

        // Act
        // Assert
        incomingChallengeGate.awaitAndThenAssert(10000) {
            collectJob.cancelAndJoin()
            val state = xAssertIs<LobbyScreenState.IncomingChallenge>(collectedState) {
                "Expected IncomingChallenge, but got ${collectedState?.javaClass?.simpleName}"
            }
            assertEquals(localTestUserInfo, state.localPlayer.info)
            assertEquals(otherTestPlayersInLobby.first(), state.challenge.challenger)
        }
    }

    @Test
    fun leaveLobby_produces_OutsideLobby() = runTest {
        // Arrange
        val sut = LobbyScreenViewModel(testLobby, localTestUserInfo)
        val insideLobbyGate = SuspendingGate()
        val outsideLobbyGate = SuspendingGate()
        var collectedState: LobbyScreenState? = null

        sut.enterLobby()
        val collectJob = launch {
            sut.screenState.collect {
                collectedState = it
                when (it) {
                    is LobbyScreenState.InsideLobby -> insideLobbyGate.open()
                    is LobbyScreenState.OutsideLobby -> outsideLobbyGate.open()
                    else -> {}
                }
            }
        }

        insideLobbyGate.await()

        // Act
        sut.leaveLobby()

        outsideLobbyGate.await()
        collectJob.cancelAndJoin()

        // Assert
        xAssertIs<LobbyScreenState.OutsideLobby>(collectedState)
    }

    @Test(expected = IllegalStateException::class)
    fun enterLobby_when_already_inside_throws() {
        val sut = LobbyScreenViewModel(testLobby, localTestUserInfo)
        sut.enterLobby()
        sut.enterLobby()
    }

    @Test(expected = IllegalStateException::class)
    fun leaveLobby_when_not_inside_throws() {
        val sut = LobbyScreenViewModel(testLobby, localTestUserInfo)
        sut.leaveLobby()
    }

    @Test(expected = IllegalStateException::class)
    fun sendChallenge_when_not_inside_throws() {
        val sut = LobbyScreenViewModel(testLobby, localTestUserInfo)
        sut.sendChallenge(otherTestPlayersInLobby.first())
    }
}

private val localTestUserInfo = UserInfo("local", "test motto")

private val otherTestPlayersInLobby: List<PlayerInfo> = buildList {
    repeat(3) {
        add(PlayerInfo(UserInfo("remote $it", "motto")))
    }
}

suspend inline fun SuspendingGate.awaitAndThenAssert(timeout: Long, block: () -> Unit) {
    try { withTimeout(timeout) { await() } }
    catch (_: TimeoutCancellationException) { }
    finally { block() }
}
