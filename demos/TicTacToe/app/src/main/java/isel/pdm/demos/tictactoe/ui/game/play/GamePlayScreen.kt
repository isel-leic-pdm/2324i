package isel.pdm.demos.tictactoe.ui.game.play

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import isel.pdm.demos.tictactoe.R
import isel.pdm.demos.tictactoe.domain.game.lobby.Challenge
import isel.pdm.demos.tictactoe.domain.game.lobby.PlayerInfo
import isel.pdm.demos.tictactoe.domain.game.play.Board
import isel.pdm.demos.tictactoe.domain.game.play.Coordinate
import isel.pdm.demos.tictactoe.domain.game.play.Game
import isel.pdm.demos.tictactoe.domain.game.play.Marker
import isel.pdm.demos.tictactoe.domain.game.play.isLocalPlayerTurn
import isel.pdm.demos.tictactoe.domain.user.UserInfo
import isel.pdm.demos.tictactoe.ui.common.TopBar
import isel.pdm.demos.tictactoe.ui.common.theme.TicTacToeTheme

/**
 * The tags used to identify the main elements of the screen used to play the game.
 */
const val GamePlayScreenTag = "GamePlayScreen"
const val GamePlayScreenTitleTag = "GamePlayScreenTitle"
const val ForfeitButtonTag = "ForfeitButton"

/**
 * The screen used to play the game.
 * Test tags for the contained [BoardView] are attributed according to its conventions.
 * @see BoardView
 */
@Composable
fun GamePlayScreen(
    state: GamePlayScreenState,
    onMoveRequested: (Coordinate) -> Unit,
    onForfeitRequested: () -> Unit,
) {
    TicTacToeTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize().testTag(GamePlayScreenTag),
            topBar = { TopBar() },
        ) { innerPadding ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            ) {
                Spacer(modifier = Modifier.height(32.dp))
                val titleTextId = when (state) {
                    is GamePlayScreenState.Starting -> R.string.game_screen_waiting_to_join
                    is GamePlayScreenState.Started ->
                        if (state.game.isLocalPlayerTurn(state.localPlayerMarker))
                            R.string.game_screen_your_turn
                        else
                            R.string.game_screen_opponent_turn
                    is GamePlayScreenState.Finished -> R.string.game_screen_match_ended
                    else -> R.string.game_screen_no_title
                }
                Text(
                    text = stringResource(id = titleTextId),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.testTag(GamePlayScreenTitleTag)
                )
                BoardView(
                    board = state.getGameBoard(),
                    onTileSelected = onMoveRequested,
                    enabled = state.isLocalPlayerTurn(),
                    modifier = Modifier
                        .padding(32.dp)
                        .weight(1.0f, true)
                        .fillMaxSize(),
                )
                Button(
                    onClick = onForfeitRequested,
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier.testTag(ForfeitButtonTag)
                ) {
                    Text(
                        text = stringResource(id = R.string.game_screen_forfeit),
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(all = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GamePlayScreenWaiting() {
    GamePlayScreen(
        state = GamePlayScreenState.Starting(
            localPlayerMarker = Marker.firstToMove,
            challenge = aChallenge
        ),
        onMoveRequested = { },
        onForfeitRequested = { }
    )
}

@Preview(showBackground = true)
@Composable
private fun GamePlayScreenMyTurnPreview() {
    GamePlayScreen(
        state = GamePlayScreenState.Started(
            game = Game.Ongoing(turn = Marker.CROSS, board = aBoard),
            localPlayerMarker = Marker.CROSS
        ),
        onMoveRequested = { },
        onForfeitRequested = { }
    )
}

@Preview(showBackground = true)
@Composable
private fun GamePlayScreenOpponentTurnPreview() {
    GamePlayScreen(
        state = GamePlayScreenState.Started(
            game = Game.Ongoing(turn = Marker.CROSS, board = aBoard),
            localPlayerMarker = Marker.CIRCLE
        ),
        onMoveRequested = { },
        onForfeitRequested = { }
    )
}

private val aBoard = Board(
    tiles = listOf(
        listOf(Marker.CROSS, null, Marker.CIRCLE),
        listOf(null, Marker.CROSS, Marker.CIRCLE),
        listOf(Marker.CIRCLE, null, Marker.CROSS)
    )
)

private val aChallenge = Challenge(
    challenger = PlayerInfo(info = UserInfo("challenger")),
    challenged = PlayerInfo(info = UserInfo("challenged"))
)


