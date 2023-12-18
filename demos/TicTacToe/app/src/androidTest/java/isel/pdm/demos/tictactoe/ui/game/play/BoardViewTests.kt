package isel.pdm.demos.tictactoe.ui.game.play

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import isel.pdm.demos.tictactoe.domain.game.play.BOARD_SIDE
import isel.pdm.demos.tictactoe.domain.game.play.Board
import isel.pdm.demos.tictactoe.domain.game.play.Coordinate
import isel.pdm.demos.tictactoe.domain.game.play.Marker
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

class BoardViewTests {

    @get:Rule
    val composeTree = createComposeRule()

    @Test
    fun selecting_an_empty_board_tile_calls_onSelected() {
        // Arrange
        var selectedTile: Coordinate? = null
        val at = Coordinate(0, 0)
        composeTree.setContent {
            BoardView(
                board = Board.EMPTY,
                onTileSelected = { selectedTile = it },
                enabled = true,
            )
        }
        // Act
        composeTree
            .onNodeWithTag(buildTagForTile(at))
            .performClick()

        // Assert
        assertEquals(at, selectedTile)
    }

    @Test
    fun board_is_correctly_displayed() {
        // Arrange
        val crossAt = Coordinate(0, 0)
        val circleAt = Coordinate(0, 2)
        composeTree.setContent {
            BoardView(
                board = Board(
                    tiles = listOf(
                        listOf(Marker.CROSS, null, Marker.CIRCLE),
                        List(size = BOARD_SIDE) { null },
                        List(size = BOARD_SIDE) { null }
                    )
                ),
                onTileSelected = { },
                enabled = true
            )
        }

        // Act
        // Assert
        composeTree
            .onNodeWithTag(buildTagForTile(crossAt, Marker.CROSS))
            .assertExists()

        composeTree
            .onNodeWithTag(buildTagForTile(circleAt, Marker.CIRCLE))
            .assertExists()
    }

    @Test
    fun selecting_empty_board_tile_on_disabled_board_does_not_call_onSelected() {
        // Arrange
        var selectedTile: Coordinate? = null
        val at = Coordinate(0, 0)
        composeTree.setContent {
            BoardView(
                board = Board.EMPTY,
                onTileSelected = { selectedTile = it },
                enabled = false,
            )
        }
        // Act
        composeTree
            .onNodeWithTag(buildTagForTile(at))
            .performClick()

        // Assert
        assertNull(selectedTile)
    }
}