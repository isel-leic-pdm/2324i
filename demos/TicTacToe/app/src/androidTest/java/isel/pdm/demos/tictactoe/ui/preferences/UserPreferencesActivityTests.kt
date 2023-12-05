package isel.pdm.demos.tictactoe.ui.preferences

import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.core.app.ActivityScenario
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import isel.pdm.demos.tictactoe.domain.user.UserInfo
import isel.pdm.demos.tictactoe.domain.user.UserInfoRepository
import isel.pdm.demos.tictactoe.ui.common.ErrorAlertTestTag
import isel.pdm.demos.tictactoe.ui.common.NavigateBackTag
import isel.pdm.demos.tictactoe.utils.PreserveDefaultDependenciesNoActivity
import isel.pdm.demos.tictactoe.utils.createPreserveDefaultDependenciesComposeRuleNoActivity
import kotlinx.coroutines.delay
import org.junit.Rule
import org.junit.Test

class UserPreferencesActivityTests {

    @get:Rule
    val testRule = createPreserveDefaultDependenciesComposeRuleNoActivity()

    private val testApplication by lazy {
        (testRule.activityRule as PreserveDefaultDependenciesNoActivity).testApplication
    }

    @Test
    fun screen_is_in_edit_mode_if_no_extra_is_passed_to_the_activity() {
        // Arrange
        ActivityScenario.launch(UserPreferencesActivity::class.java).use {
            // Act
            // Assert
            testRule.onNodeWithTag(UserPreferencesScreenTag).assertExists()
            testRule.onNodeWithTag(EditModeTestTag).assertExists()
            testRule.onNodeWithTag(ViewModeTestTag).assertDoesNotExist()
        }
    }

    @Test
    fun pressing_the_back_button_in_edit_mode_finishes_the_activity() {
        // Arrange
        ActivityScenario.launch(UserPreferencesActivity::class.java).use { scenario ->
            // Act
            testRule.onNodeWithTag(NavigateBackTag).performClick()
            // Assert
            scenario.onActivity { activity ->
                testRule.waitUntil(
                    timeoutMillis = 200,
                    condition = { activity.isFinishing }
                )
            }
        }
    }

    @Test
    fun pressing_save_button_in_edit_mode_with_valid_input_finishes_the_activity() {
        // Arrange
        val mockRepo = mockk<UserInfoRepository> {
            coEvery { updateUserInfo(any()) } returns Unit
        }
        testApplication.userInfoRepository = mockRepo
        ActivityScenario.launch(UserPreferencesActivity::class.java).use { scenario ->
            // Still Arranging
            testRule.onNodeWithTag(NickInputFieldTag).performTextInput("nick")
            testRule.onNodeWithTag(MottoInputFieldTag).performTextInput("motto")
            // Act
            testRule.onNodeWithTag(SaveButtonTag).performClick()
            // Assert
            scenario.onActivity { activity ->
                testRule.waitUntil(
                    timeoutMillis = 200,
                    condition = { activity.isFinishing }
                )
            }
        }
    }

    @Test
    fun pressing_save_button_in_edit_mode_with_valid_input_stores_the_info() {
        // Arrange
        val mockRepo = mockk<UserInfoRepository> {
            coEvery { updateUserInfo(any()) } returns Unit
        }
        testApplication.userInfoRepository = mockRepo
        val enteredNick = "nick"
        val enteredMotto = "motto"
        ActivityScenario.launch(UserPreferencesActivity::class.java).use {
            // Still Arranging
            testRule.onNodeWithTag(NickInputFieldTag).performTextInput(enteredNick)
            testRule.onNodeWithTag(MottoInputFieldTag).performTextInput(enteredMotto)
            // Act
            testRule.onNodeWithTag(SaveButtonTag).performClick()
            // Assert
            coVerify { mockRepo.updateUserInfo(UserInfo(enteredNick, enteredMotto)) }
        }
    }

    @Test
    fun an_error_message_is_displayed_if_an_error_occurs_while_saving_user_info() {
        // Arrange
        val mockRepo = mockk<UserInfoRepository> {
            coEvery { updateUserInfo(any()) } throws Exception()
        }
        testApplication.userInfoRepository = mockRepo
        val enteredNick = "nick"
        val enteredMotto = "motto"
        ActivityScenario.launch(UserPreferencesActivity::class.java).use {
            // Still Arranging
            testRule.onNodeWithTag(NickInputFieldTag).performTextInput(enteredNick)
            testRule.onNodeWithTag(MottoInputFieldTag).performTextInput(enteredMotto)
            // Act
            testRule.onNodeWithTag(SaveButtonTag).performClick()
            // Assert
            testRule.onNodeWithTag(ErrorAlertTestTag).assertExists()
        }
    }


    @Test
    fun input_fields_content_is_preserved_on_reconfiguration() {
        // Arrange
        val enteredNick = "nick"
        val enteredMotto = "motto"
        ActivityScenario.launch(UserPreferencesActivity::class.java).use { scenario ->
            // Still Arranging
            testRule.onNodeWithTag(NickInputFieldTag).performTextInput(enteredNick)
            testRule.onNodeWithTag(MottoInputFieldTag).performTextInput(enteredMotto)
            // Act
            scenario.onActivity { it.recreate() }
            // Assert
            testRule.onNodeWithTag(NickInputFieldTag).assertTextContains(enteredNick)
            testRule.onNodeWithTag(MottoInputFieldTag).assertTextContains(enteredMotto)
        }
    }

    @Test
    fun pressing_save_button_in_edit_mode_with_valid_input_finishes_the_activity_regardless_of_reconfigurations() {
        // Arrange
        val mockRepo = mockk<UserInfoRepository> {
            coEvery { updateUserInfo(any()) } coAnswers { delay(200) }
        }
        testApplication.userInfoRepository = mockRepo
        ActivityScenario.launch(UserPreferencesActivity::class.java).use { scenario ->
            // Still Arranging
            testRule.onNodeWithTag(NickInputFieldTag).performTextInput("nick")
            testRule.onNodeWithTag(MottoInputFieldTag).performTextInput("motto")
            // Act
            testRule.onNodeWithTag(SaveButtonTag).performClick()
            scenario.onActivity { it.recreate() }
            // Assert
            scenario.onActivity { activity ->
                testRule.waitUntil(
                    timeoutMillis = 500,
                    condition = { activity.isFinishing }
                )
            }
        }
    }
}