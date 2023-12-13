package isel.pdm.demos.tictactoe.ui.game.play

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import isel.pdm.demos.tictactoe.domain.game.play.Coordinate
import isel.pdm.demos.tictactoe.domain.game.play.Game
import isel.pdm.demos.tictactoe.domain.game.play.Marker
import isel.pdm.demos.tictactoe.ui.common.theme.TicTacToeTheme

/**
 * The tag used to identify the screen used to play the game.
 */
const val GamePlayScreenTag = "GamePlayScreen"

/**
 * The state of the screen used to play the game.
 * @property title The title of the screen, as a string resource identifier.
 * @property game The game being played.
 * @property localPlayerMarker The marker of the local player.
 */
data class GamePlayScreenState(
    @StringRes val title: Int?,
    val game: Game,
    val localPlayerMarker: Marker
)

/**
 * The screen used to play the game.
 */
@Composable
fun GamePlayScreen(
    state: GamePlayScreenState,
    onMoveRequested: (Coordinate) -> Unit = { },
    onForfeitRequested: () -> Unit = { },
) {
    TicTacToeTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize().testTag(GamePlayScreenTag),
        ) { innerPadding ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            ) {
            }
        }
    }
}
