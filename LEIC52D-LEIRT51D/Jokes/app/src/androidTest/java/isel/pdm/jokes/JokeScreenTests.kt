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
            JokeScreen()
        }

        composeRule.onNodeWithTag(JokeTestTag).assertDoesNotExist()
    }

    @Test
    fun screen_on_initial_state_shows_fetch_button() {
        composeRule.setContent {
            JokeScreen()
        }

        composeRule.onNodeWithTag(FetchButtonTestTag).assertExists()
    }

    @Test
    fun screen_shows_joke_after_fetch_button_click() {
        // Arrange
        composeRule.setContent {
            JokeScreen()
        }

        // Act
        composeRule.onNodeWithTag(FetchButtonTestTag).performClick()

        // Assert
        composeRule.onNodeWithTag(JokeTestTag).assertExists()
    }
}