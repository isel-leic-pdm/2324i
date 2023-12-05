package isel.pdm.demos.tictactoe.ui.game.lobby

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import isel.pdm.demos.tictactoe.TAG
import isel.pdm.demos.tictactoe.domain.game.Lobby
import isel.pdm.demos.tictactoe.domain.game.PlayerInfo
import isel.pdm.demos.tictactoe.domain.game.RosterUpdated
import isel.pdm.demos.tictactoe.domain.user.UserInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class LobbyScreenState
data object EnteringLobby : LobbyScreenState()
data class InsideLobby(val otherPlayers: List<PlayerInfo>) : LobbyScreenState()
data object OutsideLobby : LobbyScreenState()
data class LobbyAccessError(val cause: Throwable) : LobbyScreenState()

/**
 * The view model for the lobby screen.
 * @param lobby the lobby to be used.
 * @param userInfo the user information of the local player.
 */
class LobbyScreenViewModel(
    private val lobby: Lobby,
    private val userInfo: UserInfo
) : ViewModel() {

    companion object {
        fun factory(lobby: Lobby, userInfo: UserInfo) = viewModelFactory {
            initializer { LobbyScreenViewModel(lobby, userInfo) }
        }
    }

    private val _screenStateFlow: MutableStateFlow<LobbyScreenState> = MutableStateFlow(OutsideLobby)

    /**
     * The flow of states the view model traverses.
     */
    val screenState: Flow<LobbyScreenState>
        get() = _screenStateFlow.asStateFlow()

    /**
     * Enters the lobby. The state flow will be updated with the [InsideLobby] state once the
     * player is inside the lobby. In the meantime, the state flow will be updated with the
     * [EnteringLobby] state, so that other attempts to enter the lobby are immediately prevented.
     *
     * Note that, for convenience, the player is excluded from the list
     * of players in the lobby.
     */
    fun enterLobby() {
        check(_screenStateFlow.value is OutsideLobby) { "Cannot enter lobby twice" }
        _screenStateFlow.value = EnteringLobby

        viewModelScope.launch {
            Log.v(TAG, "LobbyScreenViewModel: Starting the collect coroutine on thread ${Thread.currentThread().name}")
            val localPlayerInfo = PlayerInfo(userInfo)
            try {
                lobby.enter(localPlayerInfo).collect { evt ->
                    Log.v(TAG, "LobbyScreenViewModel: Received event $evt on thread ${Thread.currentThread().name}")
                    if (evt is RosterUpdated && evt.players.contains(localPlayerInfo)) {
                        val otherPlayersInLobby = evt.players.filter { it != localPlayerInfo }
                        _screenStateFlow.value = InsideLobby(otherPlayersInLobby)
                    }
                }
            }
            catch (cause: Throwable) {
                _screenStateFlow.value = LobbyAccessError(cause)
            }
            finally {
                Log.v(TAG, "LobbyScreenViewModel: Reaching the collect coroutine on thread ${Thread.currentThread().name}")
            }
        }
    }

    /**
     * Leaves the lobby. In this case we don't need an intermediate state because the OutsideLobby
     * state does not require any additional information.
     */
    fun leaveLobby() {
        check(_screenStateFlow.value !is OutsideLobby) { "Cannot leave lobby twice" }
        _screenStateFlow.value = OutsideLobby
        viewModelScope.launch { lobby.leave() }
    }
}