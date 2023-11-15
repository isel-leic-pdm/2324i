package isel.pdm.demos.tictactoe.ui.main

import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import isel.pdm.demos.tictactoe.TicTacToeTestApplication
import isel.pdm.demos.tictactoe.utils.createActivityAndPreserveDependenciesComposeRule
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
        TODO()
    }

    @Test
    fun pressing_play_navigates_to_preferences_if_user_info_does_not_exist() {
        TODO()
    }

    @Test
    fun pressing_play_navigates_to_lobby_if_user_info_exists_regardless_of_reconfigurations() {
        TODO()
    }
}