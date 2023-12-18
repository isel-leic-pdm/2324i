package isel.pdm.demos.tictactoe.ui.game.play

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import isel.pdm.demos.tictactoe.domain.game.play.BOARD_SIDE
import isel.pdm.demos.tictactoe.domain.game.play.Board
import isel.pdm.demos.tictactoe.domain.game.play.Coordinate
import isel.pdm.demos.tictactoe.domain.game.play.Marker
import isel.pdm.demos.tictactoe.ui.common.theme.TicTacToeTheme

/**
 * Represents the view of a board.
 *
 * @param board The board to be displayed.
 * @param onTileSelected The callback to be invoked when a tile is selected.
 * @param enabled Indicates whether the board is enabled or not, that is, whether
 * the user can select a tile or not.
 * @param modifier The modifier to be applied to the view.
 */
@Composable
fun BoardView(
    board: Board,
    onTileSelected: (at: Coordinate) -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        repeat(BOARD_SIDE) { row ->
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(weight = 1.0f, fill = true)
            ) {
                repeat(BOARD_SIDE) { column ->
                    val at = Coordinate(row, column)
                    TileView(
                        move = board[at],
                        at = at,
                        enabled = enabled,
                        modifier = Modifier.weight(weight = 1.0f, fill = true),
                        onSelected = onTileSelected,
                    )
                    if (column != BOARD_SIDE - 1) { VerticalSeparator() }
                }
            }
            if (row != BOARD_SIDE - 1) { HorizontalSeparator() }
        }
    }
}

@Composable
private fun HorizontalSeparator() {
    Spacer(modifier = Modifier
        .fillMaxWidth()
        .height(8.dp)
        .background(MaterialTheme.colorScheme.secondary)
    )
}

@Composable
private fun VerticalSeparator() {
    Spacer(modifier = Modifier
        .fillMaxHeight()
        .width(8.dp)
        .background(MaterialTheme.colorScheme.secondary)
    )
}


@Preview(showBackground = true)
@Composable
private fun EmptyBoardViewPreview() {
    TicTacToeTheme {
        BoardView(board = Board.EMPTY, enabled = true, onTileSelected = { })
    }
}

@Preview(showBackground = true)
@Composable
private fun NonEmptyBoardViewPreview() {
    TicTacToeTheme {
        BoardView(board = aBoard, enabled = true, onTileSelected = { })
    }
}

private val aBoard = Board(
    tiles = listOf(
        listOf(Marker.CROSS, null, Marker.CIRCLE),
        listOf(null, Marker.CROSS, Marker.CIRCLE),
        listOf(Marker.CIRCLE, null, Marker.CROSS)
    )
)

