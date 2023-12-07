package pt.isel.pdm.tictactoe.ui.game

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import pt.isel.pdm.tictactoe.helpers.viewModelInitWithSavedState
import pt.isel.pdm.tictactoe.model.GameInfo
import pt.isel.pdm.tictactoe.ui.BaseViewModelActivity
import pt.isel.pdm.tictactoe.ui.lobby.LobbyViewModel
import pt.isel.pdm.tictactoe.ui.theme.TicTacToeTheme

class GameActivity : BaseViewModelActivity<GameViewModel>() {
    companion object {
        const val GameInfoParamName = "MY_INTENT_EXTRA"
    }

    override val viewModel: GameViewModel by viewModels {
        viewModelInitWithSavedState(this) {
            GameViewModel(
                dependencyContainer.gameService,
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val extra = intent.getParcelableExtra<GameInfo?>(GameInfoParamName)
        if (extra == null) {
            finish()
            return
        }

        viewModel.init(extra)

        safeSetContent {
            GameScreen(
                cells = viewModel.board,
                winner = viewModel.winner,
                draw = viewModel.matchEndedWithDraw,
                cellClicked = { viewModel.play(it) },
                backPressed = {finish()}
            )


        }
    }


}

