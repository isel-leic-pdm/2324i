package isel.pdm.jokes

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import java.net.URL

class JokeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun screen_initial_state_does_not_display_joke() {
        // Arrange
        // Act
        composeTestRule.setContent { JokeScreen() }
        // Assert
        composeTestRule.onNodeWithTag(JokeScreenTestTag).assertExists()
        composeTestRule.onNodeWithTag(JokeTestTag).assertDoesNotExist()
    }

    @Test
    fun screen_initial_state_displays_fetch_button() {
        // Arrange
        // Act
        composeTestRule.setContent { JokeScreen() }
        // Assert
        composeTestRule.onNodeWithTag(JokeScreenTestTag).assertExists()
        composeTestRule.onNodeWithTag(FetchItTestTag).assertExists()
    }

    @Test
    fun click_on_fetch_button_calls_jokes_service() {
        // Arrange
        val mockService = mockk<JokesService> {
            coEvery { fetchJoke() } returns Joke(text = "Chuck Norris can divide by zero.", source = URL("http://tests.com"))
        }
        composeTestRule.setContent { JokeScreen(service = mockService) }
        // Act
        composeTestRule.onNodeWithTag(FetchItTestTag).performClick()
        // Assert
        coVerify(exactly = 1) {
            mockService.fetchJoke()
        }
    }
}