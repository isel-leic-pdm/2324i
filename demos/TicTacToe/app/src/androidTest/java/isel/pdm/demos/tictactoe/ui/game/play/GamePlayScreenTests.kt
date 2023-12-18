package isel.pdm.demos.tictactoe.ui.game.play

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import isel.pdm.demos.tictactoe.R
import isel.pdm.demos.tictactoe.domain.game.lobby.Challenge
import isel.pdm.demos.tictactoe.domain.game.lobby.PlayerInfo
import isel.pdm.demos.tictactoe.domain.game.play.Board
import isel.pdm.demos.tictactoe.domain.game.play.Coordinate
import isel.pdm.demos.tictactoe.domain.game.play.Game
import isel.pdm.demos.tictactoe.domain.game.play.Marker
import isel.pdm.demos.tictactoe.domain.game.play.startGame
import isel.pdm.demos.tictactoe.domain.user.UserInfo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class GamePlayScreenTests {

    @get:Rule
    val composeTree = createComposeRule()

    @Test
    fun pressing_forfeit_button_calls_the_onForfeitRequested_callback() {
        // Arrange
        var forfeitRequested = false
        composeTree.setContent {
            GamePlayScreen(
                state = GamePlayScreenState.Started(
                    game = startGame(),
                    localPlayerMarker = Marker.firstToMove
                ),
                onMoveRequested = { },
                onForfeitRequested = { forfeitRequested = true }
            )
        }
        // Act
        composeTree.onNodeWithTag(ForfeitButtonTag).performClick()
        // Assert
        assertTrue(forfeitRequested)
    }

    @Test
    fun pressing_a_tile_calls_onMoveRequested_if_its_the_local_player_turn() {
        // Arrange
        val at = Coordinate(0, 0)
        var moveRequestedAt: Coordinate? = null
        composeTree.setContent {
            GamePlayScreen(
                state = GamePlayScreenState.Started(
                    game = startGame(),
                    localPlayerMarker = Marker.firstToMove
                ),
                onMoveRequested = { moveRequestedAt = it },
                onForfeitRequested = { }
            )
        }
        // Act
        composeTree.onNodeWithTag(buildTagForTile(at)).performClick()
        // Assert
        assertEquals(at, moveRequestedAt)
    }

    @Test
    fun pressing_a_tile_does_not_call_onMoveRequested_if_its_the_remote_player_turn() {
        // Arrange
        val at = Coordinate(0, 0)
        var moveRequestedAt: Coordinate? = null
        composeTree.setContent {
            GamePlayScreen(
                state = GamePlayScreenState.Started(
                    game = startGame(),
                    localPlayerMarker = Marker.firstToMove.other
                ),
                onMoveRequested = { moveRequestedAt = it },
                onForfeitRequested = { }
            )
        }
        // Act
        composeTree.onNodeWithTag(buildTagForTile(at)).performClick()
        // Assert
        assertNull(moveRequestedAt)
    }

    @Test
    fun when_game_is_not_started_the_screen_displays_a_waiting_message() {
        // Arrange
        var title = "NOT SET"
        composeTree.setContent {
            title = stringResource(id = R.string.game_screen_waiting_to_join)
            // Act
            GamePlayScreen(
                state = GamePlayScreenState.Starting(
                    localPlayerMarker = Marker.firstToMove,
                    challenge = Challenge(
                        challenger = PlayerInfo(info = UserInfo("challenger")),
                        challenged = PlayerInfo(info = UserInfo("challenged"))
                    )
                ),
                onMoveRequested = { },
                onForfeitRequested = { }
            )
        }
        // Assert
        composeTree.onNodeWithTag(GamePlayScreenTitleTag).assertTextContains(title)
    }

    @Test
    fun when_its_the_local_player_turn_the_screen_displays_a_message_saying_so() {
        // Arrange
        var title = "NOT SET"
        composeTree.setContent {
            title = stringResource(id = R.string.game_screen_your_turn)
            // Act
            GamePlayScreen(
                state = GamePlayScreenState.Started(
                    game = startGame(),
                    localPlayerMarker = Marker.firstToMove
                ),
                onMoveRequested = { },
                onForfeitRequested = { }
            )
        }
        // Assert
        composeTree.onNodeWithTag(GamePlayScreenTitleTag).assertTextContains(title)
    }

    @Test
    fun when_its_the_remote_player_turn_the_screen_displays_a_message_saying_so() {
        // Arrange
        var title = "NOT SET"
        composeTree.setContent {
            title = stringResource(id = R.string.game_screen_opponent_turn)
            // Act
            GamePlayScreen(
                state = GamePlayScreenState.Started(
                    game = startGame(),
                    localPlayerMarker = Marker.firstToMove.other
                ),
                onMoveRequested = { },
                onForfeitRequested = { }
            )
        }
        // Assert
        composeTree.onNodeWithTag(GamePlayScreenTitleTag).assertTextContains(title)
    }

    @Test
    fun when_the_game_is_tied_the_screen_displays_a_game_ended_message() {
        // Arrange
        var title = "NOT SET"
        composeTree.setContent {
            title = stringResource(id = R.string.game_screen_match_ended)
            // Act
            GamePlayScreen(
                state = GamePlayScreenState.Finished(
                    game = Game.Draw(
                        board = Board(
                            tiles = listOf(
                                listOf(Marker.CROSS, Marker.CIRCLE, Marker.CROSS),
                                listOf(Marker.CIRCLE, Marker.CROSS, Marker.CIRCLE),
                                listOf(Marker.CIRCLE, Marker.CROSS, Marker.CIRCLE)
                            )
                        )
                    ),
                    localPlayerMarker = Marker.firstToMove
                ),
                onMoveRequested = { },
                onForfeitRequested = { }
            )
        }
        // Assert
        composeTree.onNodeWithTag(GamePlayScreenTitleTag).assertTextContains(title)
    }

    @Test
    fun when_the_local_player_won_the_screen_displays_a_game_ended_message() {
        // Arrange
        var title = "NOT SET"
        composeTree.setContent {
            title = stringResource(id = R.string.game_screen_match_ended)
            // Act
            GamePlayScreen(
                state = GamePlayScreenState.Finished(
                    game = Game.HasWinner(
                        winner = Marker.CROSS,
                        board = Board(
                            tiles = listOf(
                                listOf(Marker.CROSS, Marker.CIRCLE, null),
                                listOf(Marker.CIRCLE, Marker.CROSS, Marker.CIRCLE),
                                listOf(null, null, Marker.CROSS)
                            )
                        )
                    ),
                    localPlayerMarker = Marker.CROSS
                ),
                onMoveRequested = { },
                onForfeitRequested = { }
            )
        }
        // Assert
        composeTree.onNodeWithTag(GamePlayScreenTitleTag).assertTextContains(title)
    }

    @Test
    fun when_the_opponent_won_the_screen_displays_a_game_ended_message() {
        // Arrange
        var title = "NOT SET"
        composeTree.setContent {
            title = stringResource(id = R.string.game_screen_match_ended)
            // Act
            GamePlayScreen(
                state = GamePlayScreenState.Finished(
                    game = Game.HasWinner(
                        winner = Marker.CIRCLE,
                        board = Board(
                            tiles = listOf(
                                listOf(Marker.CIRCLE, Marker.CROSS, null),
                                listOf(Marker.CROSS, Marker.CIRCLE, Marker.CROSS),
                                listOf(null, null, Marker.CIRCLE)
                            )
                        )
                    ),
                    localPlayerMarker = Marker.CROSS
                ),
                onMoveRequested = { },
                onForfeitRequested = { }
            )
        }
        // Assert
        composeTree.onNodeWithTag(GamePlayScreenTitleTag).assertTextContains(title)
    }
}