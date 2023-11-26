package isel.pdm.demos.tictactoe.ui.game.lobby

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import isel.pdm.demos.tictactoe.domain.game.PlayerInfo
import isel.pdm.demos.tictactoe.domain.user.UserInfo
import isel.pdm.demos.tictactoe.ui.common.NavigateBackTag
import isel.pdm.demos.tictactoe.ui.common.NavigateToPreferencesTag
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class LobbyScreenTests {

    @get:Rule
    val composeTree = createComposeRule()

    private val playerInfo = PlayerInfo(info = UserInfo("test user", "test motto"))
    private val playersInLobby = listOf(
        playerInfo,
        PlayerInfo(info = UserInfo("player2", "motto2")),
        PlayerInfo(info = UserInfo("player3", "motto3")),
    )


    @Test
    fun pressing_navigate_back_button_calls_the_onNavigateBackRequested_callback() {
        // Arrange
        var navigateBackRequested = false
        composeTree.setContent {
            LobbyScreen(
                playersInLobby = emptyList(),
                onPlayerSelected = { },
                onNavigateBackRequested = { navigateBackRequested = true },
                onNavigateToPreferencesRequested = { }
            )
        }
        // Act
        composeTree.onNodeWithTag(NavigateBackTag).performClick()
        // Assert
        assertTrue(navigateBackRequested)
    }

    @Test
    fun pressing_preferences_button_calls_the_onNavigateToPreferencesRequested_callback() {
        // Arrange
        var navigateToPreferencesRequested = false
        composeTree.setContent {
            LobbyScreen(
                playersInLobby = emptyList(),
                onPlayerSelected = { },
                onNavigateBackRequested = { },
                onNavigateToPreferencesRequested = { navigateToPreferencesRequested = true }
            )
        }
        // Act
        composeTree.onNodeWithTag(NavigateToPreferencesTag).performClick()
        // Assert
        assertTrue(navigateToPreferencesRequested)
    }

    @Test
    fun pressing_a_player_card_calls_the_onPlayerSelected_callback() {
        // Arrange
        var selectedPlayer: PlayerInfo? = null
        composeTree.setContent {
            LobbyScreen(
                playersInLobby = listOf(playerInfo),
                onPlayerSelected = { selectedPlayer = it },
                onNavigateBackRequested = { },
                onNavigateToPreferencesRequested = { }
            )
        }
        // Act
        composeTree.onNodeWithTag(PlayerInfoViewTag).performClick()
        // Assert
        assertNotNull(selectedPlayer)
        assertEquals(playerInfo, selectedPlayer)
    }

    @Test
    fun lobby_screen_displays_all_players_in_lobby() {
        // Arrange
        composeTree.setContent {
            LobbyScreen(
                playersInLobby = playersInLobby,
                onPlayerSelected = { },
                onNavigateBackRequested = { },
                onNavigateToPreferencesRequested = { }
            )
        }
        // Act
        // Assert
        composeTree.onAllNodes(hasTestTag(PlayerInfoViewTag)).assertCountEquals(3)
    }

    @Test
    fun lobby_screen_displays_no_players_if_lobby_is_empty() {
        // Arrange
        composeTree.setContent {
            LobbyScreen(
                playersInLobby = emptyList(),
                onPlayerSelected = { },
                onNavigateBackRequested = { },
                onNavigateToPreferencesRequested = { }
            )
        }
        // Act
        // Assert
        composeTree.onAllNodes(hasTestTag(PlayerInfoViewTag)).assertCountEquals(0)
    }

    @Test
    fun lobby_screen_displays_the_player_info_of_a_player_in_the_lobby() {
        // Arrange
        composeTree.setContent {
            LobbyScreen(
                playersInLobby = listOf(playerInfo),
                onPlayerSelected = { },
                onNavigateBackRequested = { },
                onNavigateToPreferencesRequested = { }
            )
        }
        // Act
        // Assert
        composeTree.onAllNodes(hasTestTag(PlayerInfoViewTag)).assertCountEquals(1)
        composeTree.onNodeWithTag(PlayerInfoViewTag)
            .assertTextContains(playerInfo.info.nick)
            .assertTextContains(playerInfo.info.motto!!)
    }
}