package isel.pdm.demos.tictactoe.ui.main

import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ActivityScenario
import io.mockk.coEvery
import io.mockk.mockk
import isel.pdm.demos.tictactoe.ui.common.ErrorAlertTestTag
import isel.pdm.demos.tictactoe.ui.game.lobby.LobbyScreenTag
import isel.pdm.demos.tictactoe.ui.preferences.UserPreferencesScreenTag
import isel.pdm.demos.tictactoe.utils.PreserveDefaultDependenciesNoActivity
import isel.pdm.demos.tictactoe.utils.createPreserveDefaultDependenciesComposeRuleNoActivity
import org.junit.Rule
import org.junit.Test

class MainActivityTests {

    @get:Rule
    val testRule = createPreserveDefaultDependenciesComposeRuleNoActivity()

    private val testApplication by lazy {
        (testRule.activityRule as PreserveDefaultDependenciesNoActivity).testApplication
    }

    @Test
    fun initially_the_main_screen_is_displayed() {
        // Arrange
        ActivityScenario.launch(MainActivity::class.java).use {
            // Act
            // Assert
            testRule.onNodeWithTag(MainScreenTag).assertExists()
        }
    }

    @Test
    fun pressing_play_navigates_to_lobby_if_user_info_exists() {
        // Arrange
        ActivityScenario.launch(MainActivity::class.java).use {
            // Act
            testRule.onNodeWithTag(PlayButtonTag).performClick()
            testRule.waitForIdle()
            // Assert
            testRule.onNodeWithTag(LobbyScreenTag).assertExists()
        }
    }

    @Test
    fun pressing_play_navigates_to_preferences_if_user_info_does_not_exist() {
        // Arrange
        testApplication.userInfoRepository = mockk {
            coEvery { getUserInfo() } returns null
        }

        ActivityScenario.launch(MainActivity::class.java).use {
            // Act
            testRule.onNodeWithTag(PlayButtonTag).performClick()
            testRule.waitForIdle()
            // Assert
            testRule.onNodeWithTag(UserPreferencesScreenTag).assertExists()
        }
    }

    @Test
    fun pressing_play_navigates_to_lobby_if_user_info_exists_regardless_of_reconfigurations() {
        // Arrange
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            // Act
            testRule.onNodeWithTag(PlayButtonTag).performClick()
            scenario.onActivity { it.recreate() }
            // Assert
            testRule.onNodeWithTag(LobbyScreenTag).assertExists()
        }
    }

    @Test
    fun an_error_message_is_displayed_if_an_error_occurs_while_loading_user_info() {
        // Arrange
        testApplication.userInfoRepository = mockk {
            coEvery { getUserInfo() } throws Exception()
        }

        ActivityScenario.launch(MainActivity::class.java).use {
            // Act
            testRule.onNodeWithTag(PlayButtonTag).performClick()
            testRule.waitForIdle()
            // Assert
            testRule.onNodeWithTag(ErrorAlertTestTag).assertExists()
        }
    }
}