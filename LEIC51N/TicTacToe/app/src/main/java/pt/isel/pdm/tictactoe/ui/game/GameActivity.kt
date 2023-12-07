package pt.isel.pdm.tictactoe.ui.game

import android.os.Bundle
import androidx.activity.viewModels
import pt.isel.pdm.tictactoe.helpers.viewModelInitWithSavedState
import pt.isel.pdm.tictactoe.model.GameInfo
import pt.isel.pdm.tictactoe.ui.BaseViewModelActivity

class GameActivity : BaseViewModelActivity<GameViewModel>() {
    companion object {
        const val GameInfoParamName = "MY_INTENT_EXTRA"
    }

    override val viewModel: GameViewModel by viewModels {
        viewModelInitWithSavedState(this) {
            GameViewModel(
                dependencyContainer.gameService,
                dependencyContainer.userStatRepository
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gameInfo = intent.getParcelableExtra<GameInfo?>(GameInfoParamName)
        if (gameInfo == null) {
            finish()
            return
        }

        viewModel.init(gameInfo)

        safeSetContent {
            GameScreen(
                cells = viewModel.board,
                winner = viewModel.winner,
                draw = viewModel.matchEndedWithDraw,
                cellClicked = { viewModel.play(it) },
                backPressed = { finish() }
            )


        }
    }


}

