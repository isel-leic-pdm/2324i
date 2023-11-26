package isel.pdm.demos.tictactoe.ui.preferences

import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import isel.pdm.demos.tictactoe.domain.user.UserInfo
import isel.pdm.demos.tictactoe.ui.common.NavigateBackTag
import isel.pdm.demos.tictactoe.utils.createActivityAndPreserveDependenciesComposeRule
import org.junit.Rule
import org.junit.Test

class UserPreferencesActivityTestsWithIntent {

    @get:Rule
    val testRule = createActivityAndPreserveDependenciesComposeRule<UserPreferencesActivity>(
        UserPreferencesActivity.createIntent(
            ctx = ApplicationProvider.getApplicationContext(),
            userInfo = UserInfo("test", "test")
        )
    )

    @Test
    fun screen_is_in_view_mode_if_userInfo_extra_is_passed_to_the_activity() {
        testRule.composeTestRule.onNodeWithTag(UserPreferencesScreenTag).assertExists()
        testRule.composeTestRule.onNodeWithTag(EditModeTestTag).assertDoesNotExist()
        testRule.composeTestRule.onNodeWithTag(ViewModeTestTag).assertExists()
    }

    @Test
    fun pressing_the_back_button_in_view_mode_finishes_the_activity() {
        // Arrange
        // Act
        testRule.composeTestRule.onNodeWithTag(NavigateBackTag).performClick()
        // Assert
        testRule.scenario.onActivity { activity ->
            testRule.composeTestRule.waitUntil(
                timeoutMillis = 200,
                condition = { activity.isFinishing }
            )
        }
    }

    @Test
    fun pressing_the_edit_button_in_view_mode_starts_the_activity_in_edit_mode() {
        // Arrange
        // Act
        testRule.composeTestRule.onNodeWithTag(EditButtonTag).performClick()
        // Assert
        testRule.composeTestRule.onNodeWithTag(UserPreferencesScreenTag).assertExists()
        testRule.composeTestRule.onNodeWithTag(EditModeTestTag).assertExists()
        testRule.composeTestRule.onNodeWithTag(ViewModeTestTag).assertDoesNotExist()
    }

    @Test
    fun once_in_edit_mode_the_screen_continues_in_that_mode_regardless_of_reconfigurations() {
        // Arrange
        testRule.composeTestRule.onNodeWithTag(EditButtonTag).performClick()
        // Act
        testRule.scenario.recreate()
        // Assert
        testRule.composeTestRule.onNodeWithTag(UserPreferencesScreenTag).assertExists()
        testRule.composeTestRule.onNodeWithTag(EditModeTestTag).assertExists()
        testRule.composeTestRule.onNodeWithTag(ViewModeTestTag).assertDoesNotExist()
    }

    @Test
    fun once_in_view_mode_the_screen_continues_in_that_mode_regardless_of_reconfigurations() {
        // Arrange
        // Act
        testRule.scenario.recreate()
        // Assert
        testRule.composeTestRule.onNodeWithTag(UserPreferencesScreenTag).assertExists()
        testRule.composeTestRule.onNodeWithTag(EditModeTestTag).assertDoesNotExist()
        testRule.composeTestRule.onNodeWithTag(ViewModeTestTag).assertExists()
    }
}