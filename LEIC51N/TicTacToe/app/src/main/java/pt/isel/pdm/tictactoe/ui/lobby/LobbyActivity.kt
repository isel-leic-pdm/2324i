package pt.isel.pdm.tictactoe.ui.lobby

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pt.isel.pdm.tictactoe.helpers.viewModelInitWithSavedState
import pt.isel.pdm.tictactoe.ui.BaseViewModelActivity
import pt.isel.pdm.tictactoe.ui.game.GameActivity
import pt.isel.pdm.tictactoe.ui.theme.TicTacToeTheme

class LobbyActivity : BaseViewModelActivity<LobbyViewModel>() {

    override val viewModel: LobbyViewModel by viewModels {
        viewModelInitWithSavedState(this) {
            LobbyViewModel(
                dependencyContainer.matchmakingService,
                dependencyContainer.userRepository
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            viewModel.gameInfo.collect { game ->
                if (game == null)
                    return@collect


                navigate<GameActivity> {
                    it.putExtra(GameActivity.GameInfoParamName, game)
                }

            }
        }

        viewModel.refreshLobbies()
        safeSetContent {

            TicTacToeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LobbyScreen(
                        lobbies = viewModel.lobbyList,
                        createNewLobby = { viewModel.startNewLobbyAndWait() },
                        refreshLobbies = { viewModel.refreshLobbies() },
                        lobbyClicked = { lobby -> viewModel.joinLobby(lobby) }
                    )
                }
            }
        }
    }
}


