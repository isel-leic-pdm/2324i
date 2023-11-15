package isel.pdm.demos.tictactoe.ui.preferences

import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import isel.pdm.demos.tictactoe.domain.UserInfo
import isel.pdm.demos.tictactoe.domain.UserInfoRepository
import isel.pdm.demos.tictactoe.ui.common.NavigateBackTag
import isel.pdm.demos.tictactoe.utils.createActivityAndPreserveDependenciesComposeRule
import org.junit.Rule
import org.junit.Test

class UserPreferencesActivityTests {

    @get:Rule
    val testRule = createActivityAndPreserveDependenciesComposeRule<UserPreferencesActivity>()

    @Test
    fun screen_is_in_edit_mode_if_no_extra_is_passed_to_the_activity() {
        testRule.composeTestRule.onNodeWithTag(UserPreferencesScreenTag).assertExists()
        testRule.composeTestRule.onNodeWithTag(EditModeTestTag).assertExists()
        testRule.composeTestRule.onNodeWithTag(ViewModeTestTag).assertDoesNotExist()
    }

    @Test
    fun pressing_the_back_button_in_edit_mode_finishes_the_activity() {
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
    fun pressing_save_button_in_edit_mode_with_valid_input_finishes_the_activity() {
        // Arrange
        val mockRepo = mockk<UserInfoRepository> {
            coEvery { updateUserInfo(any()) } returns Unit
        }
        testRule.testApplication.userInfoRepository = mockRepo
        testRule.composeTestRule.onNodeWithTag(NickInputFieldTag).performTextInput("nick")
        testRule.composeTestRule.onNodeWithTag(MottoInputFieldTag).performTextInput("motto")
        // Act
        testRule.composeTestRule.onNodeWithTag(SaveButtonTag).performClick()
        // Assert
        testRule.scenario.onActivity { activity ->
            testRule.composeTestRule.waitUntil(
                timeoutMillis = 200,
                condition = { activity.isFinishing }
            )
        }
    }

    @Test
    fun pressing_save_button_in_edit_mode_with_valid_input_stores_the_info() {
        // Arrange
        val mockRepo = mockk<UserInfoRepository> {
            coEvery { updateUserInfo(any()) } returns Unit
        }
        testRule.testApplication.userInfoRepository = mockRepo
        val enteredNick = "nick"
        val enteredMotto = "motto"
        testRule.composeTestRule.onNodeWithTag(NickInputFieldTag).performTextInput(enteredNick)
        testRule.composeTestRule.onNodeWithTag(MottoInputFieldTag).performTextInput(enteredMotto)
        // Act
        testRule.composeTestRule.onNodeWithTag(SaveButtonTag).performClick()
        // Assert
        coVerify { mockRepo.updateUserInfo(UserInfo(enteredNick, enteredMotto)) }
    }

    @Test
    fun input_fields_content_is_preserved_on_reconfiguration() {
        // Arrange
        val enteredNick = "nick"
        val enteredMotto = "motto"
        testRule.composeTestRule.onNodeWithTag(NickInputFieldTag).performTextInput(enteredNick)
        testRule.composeTestRule.onNodeWithTag(MottoInputFieldTag).performTextInput(enteredMotto)
        testRule.composeTestRule.waitForIdle()
        // Act
        testRule.scenario.recreate()
        // Assert
        testRule.composeTestRule.onNodeWithTag(NickInputFieldTag).assertTextContains(enteredNick)
        testRule.composeTestRule.onNodeWithTag(MottoInputFieldTag).assertTextContains(enteredMotto)
    }
}