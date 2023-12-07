package pt.isel.pdm.tictactoe.ui.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import pt.isel.pdm.tictactoe.R
import pt.isel.pdm.tictactoe.model.Cell
import pt.isel.pdm.tictactoe.ui.components.NavigationHandlers
import pt.isel.pdm.tictactoe.ui.components.TicTacToeBoard
import pt.isel.pdm.tictactoe.ui.components.TopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun GameScreen(
    cells: List<Cell>,
    winner: String?,
    draw: Boolean,
    cellClicked: (Cell) -> Unit,
    backPressed: () -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(
                navigationHandlers = NavigationHandlers(
                    onBackHandler = backPressed
                )
            )
        },

        ) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues)
        ) {
            TicTacToeBoard(
                cells = cells,
                cellClicked = cellClicked
            )

            if (winner != null) {
                Box(
                    modifier = Modifier
                        .background(Color.White.copy(alpha = 0.7f))
                        .fillMaxSize(),

                    )
                {
                    Column(modifier = Modifier.align(Alignment.BottomCenter)) {
                        Text(
                            text = stringResource(id = R.string.game_ended),
                            style = MaterialTheme.typography.displayMedium
                        )
                        if (draw) {
                            Text(
                                text = stringResource(id = R.string.draw),
                                style = MaterialTheme.typography.displayLarge
                            )
                        } else {
                            Text(
                                text = stringResource(id = R.string.winner),
                                style = MaterialTheme.typography.displaySmall
                            )
                            Text(
                                text = winner,
                                style = MaterialTheme.typography.displayLarge
                            )
                        }
                    }
                }
            }
        }
    }
}