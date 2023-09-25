package isel.pdm.capacitymonitor

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextEquals
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
 * We use test doubles for the [Occupancy] class, so that we can verify that the
 * [MainScreen] composable calls the appropriate methods when the buttons are clicked.
 *
 * See https://mockk.io/ for more information about the mocking library used.
 * See https://developer.android.com/training/testing/fundamentals/test-doubles for more
 * information about test doubles.
 */
class MainScreenTests {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun the_room_occupancy_is_correctly_displayed() {
        // Arrange
        val occupancy = Occupancy(10, 3)
        // Act
        composeRule.setContent { MainScreen(occupancy) }
        // Assert
        composeRule.onNodeWithTag(OccupancyTextTestTag).assertTextEquals(occupancy.current.toString())
    }

    @Test
    fun decrement_button_is_disabled_when_occupancy_is_zero() {
        // Arrange
        val occupancy = Occupancy(10, 0)
        // Act
        composeRule.setContent { MainScreen(occupancy) }
        // Assert
        composeRule.onNodeWithTag(DecrementButtonTestTag).assertIsNotEnabled()
    }

    @Test
    fun increment_button_is_disabled_when_occupancy_is_equal_to_capacity() {
        // Arrange
        val occupancy = Occupancy(10, 10)
        // Act
        composeRule.setContent { MainScreen(occupancy) }
        // Assert
        composeRule.onNodeWithTag(IncrementButtonTestTag).assertIsNotEnabled()
    }

    @Test
    fun decrement_button_is_enabled_when_occupancy_is_greater_than_zero() {
        // Arrange
        val occupancy = Occupancy(10, 1)
        // Act
        composeRule.setContent { MainScreen(occupancy) }
        // Assert
        composeRule.onNodeWithTag(DecrementButtonTestTag).assertIsEnabled()
    }

    @Test
    fun increment_button_is_enabled_when_occupancy_is_less_than_capacity() {
        // Arrange
        val occupancy = Occupancy(10, 9)
        // Act
        composeRule.setContent { MainScreen(occupancy) }
        // Assert
        composeRule.onNodeWithTag(IncrementButtonTestTag).assertIsEnabled()
    }

    @Test
    fun click_on_increment_button_increments_occupancy() {
        // Arrange
        val occupancy = spyk(Occupancy(capacity = 5, current = 2))
        composeRule.setContent { MainScreen(occupancy) }
        // Act
        composeRule.onNodeWithTag(IncrementButtonTestTag).performClick()
        // Assert
        verify(exactly = 1) {
            occupancy.inc()
        }
    }

    @Test
    fun click_on_decrement_button_decrements_occupancy() {
        // Arrange
        val occupancy = spyk(Occupancy(capacity = 5, current = 2))
        composeRule.setContent { MainScreen(occupancy) }
        // Act
        composeRule.onNodeWithTag(DecrementButtonTestTag).performClick()
        // Assert
        verify(exactly = 1) {
            occupancy.dec()
        }
    }
}
