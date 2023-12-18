package isel.pdm.demos.tictactoe.ui.game.play

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import isel.pdm.demos.tictactoe.domain.game.play.Coordinate
import isel.pdm.demos.tictactoe.domain.game.play.Marker
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class TileViewTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun clicking_an_empty_tile_calls_onSelected() {
        // Arrange
        val at = Coordinate(0, 0)
        var selected = false
        composeTestRule.setContent {
            TileView(
                move = null,
                at = at,
                enabled = true,
                onSelected = { selected = true },
            )
        }

        // Act
        composeTestRule.onNodeWithTag(buildTagForTile(at)).performClick()

        // Assert
        assertTrue(selected)
    }

    @Test
    fun clicking_an_empty_tile_does_not_call_onSelected_if_disabled() {
        // Arrange
        val at = Coordinate(0, 0)
        var selected = false
        composeTestRule.setContent {
            TileView(
                move = null,
                at = at,
                enabled = false,
                onSelected = { selected = true },
            )
        }

        // Act
        composeTestRule.onNodeWithTag(buildTagForTile(at)).performClick()

        // Assert
        assertTrue(!selected)
    }

    @Test
    fun clicking_an_non_empty_tile_does_not_call_onSelected() {
        // Arrange
        val at = Coordinate(0, 0)
        var selected = false
        val marker = Marker.CIRCLE
        composeTestRule.setContent {
            TileView(move =marker, at = at, onSelected = { selected = true })
        }

        // Act
        composeTestRule.onNodeWithTag(buildTagForTile(at, marker)).performClick()

        // Assert
        assertFalse(selected)
    }

    @Test
    fun circle_is_correctly_displayed() {
        // Arrange
        val at = Coordinate(0, 0)
        val marker = Marker.CIRCLE
        composeTestRule.setContent {
            TileView(move = marker, at = at, enabled = true, onSelected = { })
        }

        // Act
        // Assert
        composeTestRule
            .onNodeWithTag(buildTagForTile(at, marker))
            .assertExists()
    }

    @Test
    fun cross_is_correctly_displayed() {
        // Arrange
        val at = Coordinate(0, 0)
        val marker = Marker.CROSS
        composeTestRule.setContent {
            TileView(move = marker, at = at, enabled = true, onSelected = { })
        }

        // Act
        // Assert
        composeTestRule
            .onNodeWithTag(buildTagForTile(at, marker))
            .assertExists()
    }

    @Test
    fun empty_tile_is_correctly_displayed() {
        // Arrange
        val at = Coordinate(0, 0)
        composeTestRule.setContent {
            TileView(move = null, at = at, enabled = true, onSelected = { })
        }

        // Act
        // Assert
        composeTestRule
            .onNodeWithTag(buildTagForTile(at))
            .assertExists()
    }
}
