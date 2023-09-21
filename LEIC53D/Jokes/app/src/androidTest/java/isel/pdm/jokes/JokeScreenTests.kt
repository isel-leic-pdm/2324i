package isel.pdm.jokes

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class JokeScreenTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun screen_in_initial_state_does_not_display_joke() {
        composeTestRule.setContent {
            JokeScreen()
        }

        composeTestRule.onNodeWithTag(JokeTestTag).assertDoesNotExist()
    }

    @Test
    fun when_fetch_button_is_pressed_joke_is_displayed() {
        composeTestRule.setContent {
            JokeScreen()
        }

        composeTestRule.onNodeWithTag(FetchItTestTag).performClick()
        composeTestRule.onNodeWithTag(JokeTestTag).assertExists()
    }
}