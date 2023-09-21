package isel.pdm.jokes

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import isel.pdm.jokes.ui.theme.JokesTheme
import org.junit.Rule
import org.junit.Test

class JokeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun screen_initial_state_does_not_display_joke() {
        // Start the app
        composeTestRule.setContent {
            JokesTheme {
                JokeScreen()
            }
        }

        composeTestRule.onNodeWithTag(JokeScreenTestTag).assertExists()
        composeTestRule.onNodeWithTag(JokeTestTag).assertDoesNotExist()
    }

    @Test
    fun screen_initial_state_displays_fetch_button() {
        // Start the app
        composeTestRule.setContent {
            JokesTheme {
                JokeScreen()
            }
        }

        composeTestRule.onNodeWithTag(JokeScreenTestTag).assertExists()
        composeTestRule.onNodeWithTag(FetchItTestTag).assertExists()
    }

    @Test
    fun after_fetch_button_is_pressed_joke_is_displayed() {
        // Start the app
        composeTestRule.setContent {
            JokesTheme {
                JokeScreen()
            }
        }

        composeTestRule.onNodeWithTag(JokeScreenTestTag).assertExists()
        composeTestRule.onNodeWithTag(FetchItTestTag).performClick()
        composeTestRule.onNodeWithTag(JokeTestTag).assertExists()
    }
}