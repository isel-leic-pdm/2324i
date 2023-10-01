package isel.pdm.stopwatch

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

/**
 * Instrumented tests for the [StopWatchControl] composable.
 */
class StopWatchControlTests {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun start_button_is_available_when_the_stopwatch_is_stopped() {
        // Arrange
        val stopwatch = StopWatch(startedAt = 0, stoppedAt = 0)
        // Act
        composeRule.setContent { StopWatchControl(stopwatch) }
        // Assert
        composeRule.onNodeWithTag(START_BUTTON_TEST_TAG).assertIsEnabled()
    }

    @Test
    fun stop_button_is_unavailable_when_the_stopwatch_is_stopped() {
        // Arrange
        val stopwatch = StopWatch(startedAt = 0, stoppedAt = 0)
        // Act
        composeRule.setContent { StopWatchControl(stopwatch) }
        // Assert
        composeRule.onNodeWithTag(STOP_BUTTON_TEST_TAG).assertDoesNotExist()
    }

    @Test
    fun reset_button_is_disabled_when_the_stopwatch_is_stopped() {
        // Arrange
        val stopwatch = StopWatch(startedAt = 0, stoppedAt = 0)
        // Act
        composeRule.setContent { StopWatchControl(stopwatch) }
        // Assert
        composeRule.onNodeWithTag(RESET_BUTTON_TEST_TAG).assertIsNotEnabled()
    }

    @Test
    fun start_button_is_unavailable_when_the_stopwatch_is_started() {
        // Arrange
        val stopwatch = StopWatch(startedAt = 0, stoppedAt = 0).start()
        // Act
        composeRule.setContent { StopWatchControl(stopwatch) }
        // Assert
        composeRule.onNodeWithTag(START_BUTTON_TEST_TAG).assertDoesNotExist()
    }

    @Test
    fun stop_button_is_available_when_the_stopwatch_is_started() {
        // Arrange
        val stopwatch = StopWatch(startedAt = 0, stoppedAt = 0).start()
        // Act
        composeRule.setContent { StopWatchControl(stopwatch) }
        // Assert
        composeRule.onNodeWithTag(STOP_BUTTON_TEST_TAG).assertIsEnabled()
    }

    @Test
    fun pressing_start_button_calls_onStart_when_stopwatch_is_stopped_and_its_value_is_zero() {
        // Arrange
        val stopwatch = StopWatch(startedAt = 0, stoppedAt = 0)
        var onStartCalled = false
        // Act
        composeRule.setContent {
            StopWatchControl(stopwatch, onStart = { onStartCalled = true })
        }
        composeRule.onNodeWithTag(START_BUTTON_TEST_TAG).performClick()
        // Assert
        assertTrue(onStartCalled)
    }

    @Test
    fun pressing_stop_button_calls_onStop_when_stopwatch_is_started() {
        // Arrange
        val stopwatch = StopWatch(startedAt = 0, stoppedAt = 0).start()
        var onStopCalled = false
        // Act
        composeRule.setContent {
            StopWatchControl(stopwatch, onStop = { onStopCalled = true })
        }
        composeRule.onNodeWithTag(STOP_BUTTON_TEST_TAG).performClick()
        // Assert
        assertTrue(onStopCalled)
    }

    @Test
    fun pressing_start_button_on_non_zero_stopped_stopwatch_calls_onResume() {
        // Arrange
        val stopwatch = StopWatch(startedAt = 0, stoppedAt = 65021)
        var onResumeCalled = false
        // Act
        composeRule.setContent {
            StopWatchControl(stopwatch, onResume = { onResumeCalled = true })
        }
        composeRule.onNodeWithTag(START_BUTTON_TEST_TAG).performClick()
        // Assert
        assertTrue(onResumeCalled)
    }

    @Test
    fun pressing_reset_button_calls_onReset_when_stopwatch_is_stopped_and_its_value_is_non_zero() {
        // Arrange
        val stopwatch = StopWatch(startedAt = 0, stoppedAt = 65021)
        var onResetCalled = false
        // Act
        composeRule.setContent {
            StopWatchControl(stopwatch, onReset = { onResetCalled = true })
        }
        composeRule.onNodeWithTag(RESET_BUTTON_TEST_TAG).performClick()
        // Assert
        assertTrue(onResetCalled)
    }
}