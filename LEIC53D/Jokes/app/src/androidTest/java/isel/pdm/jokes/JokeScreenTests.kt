package isel.pdm.jokes

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import isel.pdm.jokes.daily.FetchItTestTag
import isel.pdm.jokes.daily.JokeScreen
import isel.pdm.jokes.daily.JokeTestTag
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.net.URL

class JokeScreenTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun screen_initial_state_does_not_display_joke() {
        // Arrange
        // Act
        composeTestRule.setContent { JokeScreen() }
        // Assert
        composeTestRule.onNodeWithTag(JokeTestTag).assertDoesNotExist()
    }

    @Test
    fun screen_initial_state_displays_fetch_button() {
        // Arrange
        // Act
        composeTestRule.setContent { JokeScreen() }
        // Assert
        composeTestRule.onNodeWithTag(FetchItTestTag).assertIsDisplayed()
    }

    @Test
    fun click_on_fetch_button_calls_onFetchRequested() {
        // Arrange
        var called = false
        // Act
        composeTestRule.setContent {
            JokeScreen(onFetchRequested = { called = true })
        }
        composeTestRule.onNodeWithTag(FetchItTestTag).performClick()
        // Assert
        assertTrue(called)
    }
}