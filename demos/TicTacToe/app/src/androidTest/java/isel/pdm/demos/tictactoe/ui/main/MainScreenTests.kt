package isel.pdm.demos.tictactoe.ui.main

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class MainScreenTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun main_screen_displays_play_button() {
        // Arrange
        composeTestRule.setContent {
            MainScreen(onPlayRequested = { })
        }
        // Act
        // Assert
        composeTestRule.onNodeWithTag(PlayButtonTag).assertExists()
        composeTestRule.onNodeWithTag(PlayButtonTag).assertIsEnabled()
    }

    @Test
    fun pressing_play_calls_onPlayRequested_callback() {
        // Arrange
        var playRequested = false
        composeTestRule.setContent {
            MainScreen(onPlayRequested = { playRequested = true })
        }
        // Act
        composeTestRule.onNodeWithTag(PlayButtonTag).performClick()
        // Assert
        assertTrue(playRequested)
    }
}
