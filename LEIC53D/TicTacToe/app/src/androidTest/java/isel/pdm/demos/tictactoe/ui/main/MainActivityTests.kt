package isel.pdm.demos.tictactoe.ui.main

import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import io.mockk.coEvery
import io.mockk.mockk
import isel.pdm.demos.tictactoe.TicTacToeTestApplication
import isel.pdm.demos.tictactoe.domain.UserInfo
import isel.pdm.demos.tictactoe.domain.UserInfoRepository
import isel.pdm.demos.tictactoe.ui.game.lobby.LobbyScreenTag
import isel.pdm.demos.tictactoe.ui.preferences.UserPreferencesScreenTag
import isel.pdm.demos.tictactoe.utils.createActivityAndPreserveDependenciesComposeRule
import kotlinx.coroutines.delay
import org.junit.Rule
import org.junit.Test

class MainActivityTests {

    @get:Rule
    val testRule = createActivityAndPreserveDependenciesComposeRule<MainActivity>()

    /**
     * Shortcut to the [TicTacToeTestApplication] instance, used to access the dependencies.
     */
    private val testApplication: TicTacToeTestApplication
        get() = testRule.testApplication

    /**
     * Shortcut to the [ComposeTestRule] instance, used to access the composition tree.
     */
    private val composeTree: ComposeTestRule
        get() = testRule.composeTestRule

    @Test
    fun initially_the_main_screen_is_displayed() {
        composeTree.onNodeWithTag(MainScreenTag).assertExists()
    }

    @Test
    fun pressing_play_navigates_to_lobby_if_user_info_exists() {
        composeTree.onNodeWithTag(PlayButtonTag).performClick()
        composeTree.waitForIdle()
        composeTree.onNodeWithTag(LobbyScreenTag).assertExists()
    }

    @Test
    fun pressing_play_navigates_to_preferences_if_user_info_does_not_exist() {
        // Arrange
        testApplication.userInfoRepository = mockk {
            coEvery { getUserInfo() } returns null
        }

        // Act
        composeTree.onNodeWithTag(PlayButtonTag).performClick()
        composeTree.waitForIdle()
        // Assert
        composeTree.onNodeWithTag(UserPreferencesScreenTag).assertExists()
    }

    @Test
    fun pressing_play_navigates_to_lobby_if_user_info_exists_regardless_of_reconfigurations() {
        // Arrange
        testApplication.userInfoRepository = mockk {
            coEvery { getUserInfo() } coAnswers {
                delay(1000)
                UserInfo("test nickname", "the motto")
            }
        }

        // Act
        composeTree.onNodeWithTag(PlayButtonTag).performClick()
        testRule.scenario.recreate()
        composeTree.waitForIdle()

        // Assert
        composeTree.onNodeWithTag(LobbyScreenTag).assertExists()
    }
}