package pt.isel.pdm.tictactoe.ui.lobby

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import pt.isel.pdm.tictactoe.model.GameInfo
import pt.isel.pdm.tictactoe.model.GameLobby
import pt.isel.pdm.tictactoe.model.UserInfo
import pt.isel.pdm.tictactoe.repository.UserRepository
import pt.isel.pdm.tictactoe.services.MatchmakingService
import pt.isel.pdm.tictactoe.ui.BaseViewModel

class LobbyViewModel(
    private val service: MatchmakingService,
    private val userRepo: UserRepository
) : BaseViewModel() {

    private var _currUsr: UserInfo? = null

    private val _gameInfoFlow = MutableStateFlow<GameInfo?>(null)

    val gameInfo: Flow<GameInfo?>
        get() = _gameInfoFlow

    var lobbyList by mutableStateOf<List<GameLobby>>(listOf())

    fun refreshLobbies() {
        loadingAndErrorAwareScope {
            lobbyList = service.getLobbies()
        }
    }

    fun startNewLobbyAndWait() {
        loadingAndErrorAwareScope {
            ignoreCancelationException(this) {
                EnsureUser()
                val session = service.createLobbyAndWaitForPlayer(_currUsr!!.userName)
                _gameInfoFlow.value = GameInfo(session.gameId, true)
            }
        }
    }

    fun joinLobby(lobby: GameLobby) {
        loadingAndErrorAwareScope {
            ignoreCancelationException(this) {
                EnsureUser()
                val session = service.joinLobby(_currUsr!!.userName, lobby)
                _gameInfoFlow.value = GameInfo(session.gameId, false)
            }
        }
    }

    private suspend fun EnsureUser() {
        if (_currUsr != null)
            return

        _currUsr = userRepo.getUserData()

        if (_currUsr == null)
            throw Exception("No user selected")
    }

    fun cancelPendingRequests() {
        viewModelScope.coroutineContext.cancelChildren()
    }

}