package isel.pdm.jokes

import org.junit.Rule
import org.junit.Test

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick

class JokeScreenTests {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun screen_on_initial_state_does_not_show_joke() {
        composeRule.setContent {
            JokeScreen(service = NoOpJokeService)
        }

        composeRule.onNodeWithTag(JokeTestTag).assertDoesNotExist()
    }

    @Test
    fun screen_on_initial_state_shows_fetch_button() {
        composeRule.setContent {
            JokeScreen(service = NoOpJokeService)
        }

        composeRule.onNodeWithTag(FetchItTestTag).assertExists()
    }

    @Test
    fun screen_shows_joke_after_fetch_button_click() {
        // Arrange
        composeRule.setContent {
            JokeScreen(service = NoOpJokeService)
        }

        // Act
        composeRule.onNodeWithTag(FetchItTestTag).performClick()

        // Assert
        composeRule.onNodeWithTag(JokeTestTag).assertExists()
    }
}