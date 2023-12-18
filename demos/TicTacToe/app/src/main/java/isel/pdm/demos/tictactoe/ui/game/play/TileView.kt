package isel.pdm.demos.tictactoe.ui.game.play

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import isel.pdm.demos.tictactoe.R
import isel.pdm.demos.tictactoe.domain.game.play.Coordinate
import isel.pdm.demos.tictactoe.domain.game.play.Marker
import isel.pdm.demos.tictactoe.ui.common.theme.TicTacToeTheme

/**
 * Helper function to build the test tag of a tile at the given position and with the given marker.
 */
fun buildTagForTile(at: Coordinate, marker: Marker? = null) =
    "TileView_${at.row}:${at.column}${
        when (marker) {
            null -> ""
            Marker.CIRCLE -> "Circle"
            Marker.CROSS -> "Cross"
        }
    }"

/**
 * The view used to display a tile of the board.
 *
 * @param modifier The modifier to be applied to the view.
 * @param move The marker to display in the tile, or null if the tile is empty.
 * @param at The position of the tile in the board.
 * @param enabled Whether the tile is enabled or not. If the tile is not empty,
 * it is always disabled.
 * @param onSelected The callback to invoke when the tile is selected. It is only
 * invoked if the tile is enabled.
 */
@Composable
fun TileView(
    modifier: Modifier = Modifier,
    move: Marker?,
    at: Coordinate,
    enabled: Boolean = false,
    onSelected: (at: Coordinate) -> Unit,
) {
    Box(modifier = modifier
        .background(MaterialTheme.colorScheme.background)
        .fillMaxSize(1.0f)
        .padding(12.dp)
        .clickable(enabled = move == null && enabled) { onSelected(at) }
        .testTag(buildTagForTile(at, move))
    ) {
        if (move != null) {
            Image(
                painter = painterResource(
                    id = when (move) {
                        Marker.CIRCLE -> R.drawable.circle_red
                        Marker.CROSS -> R.drawable.cross_red
                    }
                ),
                contentDescription = "",
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TileViewCirclePreview() {
    TicTacToeTheme {
        TileView(
            move = Marker.CIRCLE,
            at = Coordinate(0, 0),
            enabled = true,
            onSelected = { _ -> },
            modifier = Modifier.size(200.dp)
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun TileViewCrossPreview() {
    TicTacToeTheme {
        TileView(
            move = Marker.CROSS,
            at = Coordinate(0, 0),
            enabled = true,
            onSelected = { },
            modifier = Modifier.size(200.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TileViewEmptyPreview() {
    TicTacToeTheme {
        TileView(
            move = null,
            at = Coordinate(0, 0),
            enabled = true,
            onSelected = { },
            modifier = Modifier.size(200.dp)
        )
    }
}
