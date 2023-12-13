package isel.pdm.demos.tictactoe.ui.game.lobby

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import isel.pdm.demos.tictactoe.TAG
import isel.pdm.demos.tictactoe.domain.game.lobby.Challenge
import isel.pdm.demos.tictactoe.domain.game.lobby.ChallengeReceived
import isel.pdm.demos.tictactoe.domain.game.lobby.Lobby
import isel.pdm.demos.tictactoe.domain.game.lobby.PlayerInfo
import isel.pdm.demos.tictactoe.domain.game.lobby.RosterUpdated
import isel.pdm.demos.tictactoe.domain.user.UserInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 *  Sum type that represents the possible states of the lobby screen..
 *
 *  [EnteringLobby] means that the player is entering the lobby.
 *  [InsideLobby] means that the player is inside the lobby.
 *  [OutsideLobby] means that the player is outside the lobby.
 *  [LobbyAccessError] means that an error occurred while accessing the lobby.
 *  [IncomingChallenge] means that a match is about to start because a remote player challenged
 *  the local player
 *  [SentChallenge] means that a match is about to start because the local player challenged
 *  another player in the lobby
 */
sealed class LobbyScreenState
data object EnteringLobby : LobbyScreenState()
data class InsideLobby(val localPlayer: PlayerInfo, val otherPlayers: List<PlayerInfo>) : LobbyScreenState()
data object OutsideLobby : LobbyScreenState()
data class LobbyAccessError(val cause: Throwable) : LobbyScreenState()
data class IncomingChallenge(val localPlayer: PlayerInfo, val challenge: Challenge) : LobbyScreenState()
data class SentChallenge(val localPlayer: PlayerInfo, val challenge: Challenge) : LobbyScreenState()

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
     * @throws IllegalStateException if the player is already inside the lobby.
     */
    fun enterLobby() {
        check(_screenStateFlow.value is OutsideLobby) { "Cannot enter lobby twice" }
        _screenStateFlow.value = EnteringLobby

        viewModelScope.launch {
            val localPlayerInfo = PlayerInfo(userInfo)
            try {
                lobby.enter(localPlayerInfo).collect { evt ->
                    if (evt is RosterUpdated && evt.players.contains(localPlayerInfo)) {
                        val otherPlayersInLobby = evt.players.filter { it != localPlayerInfo }
                        _screenStateFlow.value = InsideLobby(localPlayerInfo, otherPlayersInLobby)
                    }
                    if (evt is ChallengeReceived) {
                        _screenStateFlow.value = IncomingChallenge(localPlayerInfo, evt.challenge)
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
     *  Sends a challenge to the given player. The state flow will be updated with the [SentChallenge].
     *  @param player the player to which the challenge will be sent.
     *  @throws IllegalStateException if the player is not inside the lobby.
     */
    fun sendChallenge(player: PlayerInfo) {
        check(_screenStateFlow.value is InsideLobby) { "Cannot send challenge until inside the lobby" }
        viewModelScope.launch {
            val localPlayer = (_screenStateFlow.value as InsideLobby).localPlayer
            val challenge = lobby.issueChallenge(to = player)
            _screenStateFlow.value = SentChallenge(localPlayer, challenge)
        }
    }
    /**
     * Leaves the lobby. In this case we don't need an intermediate state because the OutsideLobby
     * state does not require any additional information.
     * @throws IllegalStateException if the player is not inside the lobby.
     */
    fun leaveLobby() {
        check(_screenStateFlow.value !is OutsideLobby) { "Cannot leave lobby twice" }
        _screenStateFlow.value = OutsideLobby
        viewModelScope.launch { lobby.leave() }
    }
}