package isel.pdm.stopwatch

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasTextExactly
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import io.mockk.spyk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test

/**
 * Instrumented tests for the [MainScreen] composable.
 *
 * We use test doubles for the [StopWatch] class, so that we can verify that the
 * [MainScreen] composable calls the appropriate methods when the buttons are clicked.
 *
 * See https://mockk.io/ for more information about the mocking library used.
 * See https://developer.android.com/training/testing/fundamentals/test-doubles for more
 * information about test doubles.
 */
@ExperimentalTestApi
class MainScreenTests {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun the_stopwatch_value_is_correctly_displayed() {
        // Arrange
        val stopwatch = StopWatch(startedAt = 0, stoppedAt = 65210)
        // Act
        composeRule.setContent { MainScreen(stopwatch) }
        // Assert
        composeRule.onNodeWithTag(MinutesTestTag).assertTextEquals("01:")
        composeRule.onNodeWithTag(SecondsTestTag).assertTextEquals("05,")
        composeRule.onNodeWithTag(DeciSecondsTestTag).assertTextEquals("21")
    }

    @Test
    fun the_stopwatch_value_is_zeroed_when_reset_button_is_clicked() {
        // Arrange
        val stopwatch = spyk(StopWatch(startedAt = 0, stoppedAt = 65210))
        // Act
        composeRule.setContent { MainScreen(stopwatch) }
        composeRule.onNodeWithTag(RESET_BUTTON_TEST_TAG).performClick()
        // Assert
        composeRule.onNodeWithTag(MinutesTestTag).assertTextEquals("00:")
        composeRule.onNodeWithTag(SecondsTestTag).assertTextEquals("00,")
        composeRule.onNodeWithTag(DeciSecondsTestTag).assertTextEquals("00")
    }

    @Test
    fun start_of_zero_valued_stopWatch_is_called_when_start_button_is_clicked() {
        // Arrange
        val stopwatch = spyk(StopWatch(startedAt = 0, stoppedAt = 0))
        // Act
        composeRule.setContent { MainScreen(stopwatch) }
        composeRule.onNodeWithTag(START_BUTTON_TEST_TAG).performClick()
        // Assert
        verify(exactly = 1) { stopwatch.start() }
    }

    @Test
    fun stop_of_stopWatch_is_called_when_stop_button_is_clicked() {
        // Arrange
        val stopwatch = spyk(StopWatch(startedAt = 0, stoppedAt = null))
        // Act
        composeRule.setContent { MainScreen(stopwatch) }
        composeRule.onNodeWithTag(STOP_BUTTON_TEST_TAG).performClick()
        // Assert
        verify(exactly = 1) { stopwatch.stop() }
    }

    @Test
    fun resume_of_non_zero_valued_stopWatch_is_called_when_start_button_is_clicked() {
        // Arrange
        val stopwatch = spyk(StopWatch(startedAt = 0, stoppedAt = 65210))
        // Act
        composeRule.setContent { MainScreen(stopwatch) }
        composeRule.onNodeWithTag(START_BUTTON_TEST_TAG).performClick()
        // Assert
        verify(exactly = 1) { stopwatch.resume() }
    }

    @Test
    fun clicking_the_start_button_enables_periodic_update_of_the_stopwatch_value() {
        // Arrange
        val stopwatch = StopWatch(startedAt = 0, stoppedAt = 0)
        // Act
        composeRule.setContent { MainScreen(stopwatch) }
        composeRule.onNodeWithTag(START_BUTTON_TEST_TAG).performClick()
        // Assert
        composeRule.waitUntilDoesNotExist(
            matcher = hasTextExactly("00"),
            timeoutMillis = 500
        )
    }

    @Test
    fun clicking_the_reset_button_resets_the_stopwatch_value() {
        // Arrange
        val stopwatch = StopWatch(startedAt = 0, stoppedAt = 65210)
        // Act
        composeRule.setContent { MainScreen(stopwatch) }
        composeRule.onNodeWithTag(RESET_BUTTON_TEST_TAG).performClick()
        // Assert
        composeRule.onNodeWithTag(MinutesTestTag).assertTextEquals("00:")
        composeRule.onNodeWithTag(SecondsTestTag).assertTextEquals("00,")
        composeRule.onNodeWithTag(DeciSecondsTestTag).assertTextEquals("00")
    }
}