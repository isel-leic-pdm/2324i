package isel.pdm.demos.tictactoe.ui.game.lobby

import android.util.Log
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import isel.pdm.demos.tictactoe.domain.game.Lobby
import isel.pdm.demos.tictactoe.domain.game.PlayerInfo
import isel.pdm.demos.tictactoe.domain.game.RosterUpdated
import isel.pdm.demos.tictactoe.domain.user.UserInfo
import isel.pdm.demos.tictactoe.utils.MockMainDispatcherRule
import isel.pdm.demos.tictactoe.utils.SuspendingGate
import isel.pdm.demos.tictactoe.utils.xAssertIs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
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

    private val testLobby = mockk<Lobby> {
        val localPlayer = slot<PlayerInfo>()
        coEvery { enter(capture(localPlayer)) } returns flow {
            emit(RosterUpdated(
                buildList {
                    add(localPlayer.captured)
                    addAll(otherTestPlayersInLobby)
                }
            ))
        }

        coEvery { leave() } returns Unit
    }

    @Test
    fun enterLobby_produces_InsideLobby_with_the_local_player_excluded_from_the_list() = runTest {
        // Arrange
        val sut = LobbyScreenViewModel(testLobby, localTestUserInfo)
        val insideLobbyGate = SuspendingGate()
        var collectedState: InsideLobby? = null

        // Act
        val collectJob = launch {
            sut.enterLobby()
            sut.screenState.collect {
                if (it is InsideLobby) {
                    collectedState = it
                    insideLobbyGate.open()
                }
            }
        }

        insideLobbyGate.await()
        collectJob.cancelAndJoin()

        // Assert
        val state = xAssertIs<InsideLobby>(collectedState)
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
                if (it is LobbyAccessError)
                    errorGate.open()
            }
        }

        // Act
        sut.enterLobby()

        errorGate.await()
        collectJob.cancelAndJoin()

        // Assert
        val state = xAssertIs<LobbyAccessError>(collectedState)
        assertEquals(expectedException.message, state.cause.message)
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
                    is InsideLobby -> insideLobbyGate.open()
                    is OutsideLobby -> outsideLobbyGate.open()
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
        xAssertIs<OutsideLobby>(collectedState)
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
}

private val localTestUserInfo = UserInfo("local", "test motto")

private val otherTestPlayersInLobby: List<PlayerInfo> = buildList {
    repeat(3) {
        add(PlayerInfo(UserInfo("remote $it", "motto")))
    }
}
