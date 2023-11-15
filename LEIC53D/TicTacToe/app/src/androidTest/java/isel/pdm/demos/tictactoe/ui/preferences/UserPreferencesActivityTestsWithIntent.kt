package isel.pdm.demos.tictactoe.ui.preferences

import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import isel.pdm.demos.tictactoe.domain.UserInfo
import isel.pdm.demos.tictactoe.ui.common.NavigateBackTag
import isel.pdm.demos.tictactoe.utils.createActivityAndPreserveDependenciesComposeRule
import org.junit.Rule
import org.junit.Test

class UserPreferencesActivityTestsWithIntent {

    @get:Rule
    val testRule = createActivityAndPreserveDependenciesComposeRule<UserPreferencesActivity>(
        // TODO: Uncomment the following lines
//        UserPreferencesActivity.createIntent(
//            ctx = ApplicationProvider.getApplicationContext(),
//            userInfo = UserInfo("test", "test")
//        )
    )

    @Test
    fun screen_is_in_view_mode_if_userInfo_extra_is_passed_to_the_activity() {
        TODO()
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
}