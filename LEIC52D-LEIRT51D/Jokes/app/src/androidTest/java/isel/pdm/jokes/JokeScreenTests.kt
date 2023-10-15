package isel.pdm.jokes

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import isel.pdm.jokes.daily.FetchItTestTag
import isel.pdm.jokes.daily.JokeScreen
import isel.pdm.jokes.daily.JokeTestTag
import isel.pdm.jokes.ui.ErrorAlertTestTag
import isel.pdm.jokes.ui.NavigateToInfoTestTag
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
    fun screen_with_joke_displays_joke() {
        // Arrange
        val aJoke = Joke(text = "The joke", source = URL("https://www.tests.com"))
        // Act
        composeTestRule.setContent { JokeScreen(joke = LoadState.success(aJoke)) }
        // Assert
        composeTestRule.onNodeWithTag(JokeTestTag).assertIsDisplayed()
    }

    @Test
    fun screen_with_error_displays_error_alert() {
        // Arrange
        // Act
        composeTestRule.setContent { JokeScreen(joke = LoadState.failure(Exception("Error"))) }
        // Assert
        composeTestRule.onNodeWithTag(ErrorAlertTestTag).assertIsDisplayed()
    }

    @Test
    fun click_on_fetch_button_calls_jokes_service() {
        // Arrange
        var fetchClicked = false
        composeTestRule.setContent { JokeScreen(joke = Idle, onFetch = { fetchClicked = true }) }
        // Act
        composeTestRule.onNodeWithTag(FetchItTestTag).performClick()
        // Assert
        assertTrue(fetchClicked)
    }

    @Test
    fun click_on_navigation_to_about_screen_calls_callback() {
        // Arrange
        var infoClicked = false
        composeTestRule.setContent { JokeScreen(joke = Idle, onInfoRequested = { infoClicked = true }) }
        // Act
        composeTestRule.onNodeWithTag(NavigateToInfoTestTag).performClick()
        // Assert
        assertTrue(infoClicked)
    }
}