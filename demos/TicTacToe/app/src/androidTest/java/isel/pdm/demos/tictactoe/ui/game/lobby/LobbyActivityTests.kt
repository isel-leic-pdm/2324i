package isel.pdm.demos.tictactoe.ui.game.lobby

import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ActivityScenario
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import isel.pdm.demos.tictactoe.domain.game.lobby.Challenge
import isel.pdm.demos.tictactoe.domain.game.lobby.ChallengeReceived
import isel.pdm.demos.tictactoe.domain.game.lobby.PlayerInfo
import isel.pdm.demos.tictactoe.domain.game.lobby.RosterUpdated
import isel.pdm.demos.tictactoe.domain.user.UserInfo
import isel.pdm.demos.tictactoe.infrastructure.otherTestPlayersInLobby
import isel.pdm.demos.tictactoe.ui.common.ErrorAlertDismissButtonTestTag
import isel.pdm.demos.tictactoe.ui.common.ErrorAlertTestTag
import isel.pdm.demos.tictactoe.ui.common.NavigateBackTag
import isel.pdm.demos.tictactoe.ui.common.NavigateToPreferencesTag
import isel.pdm.demos.tictactoe.ui.game.play.GamePlayScreenTag
import isel.pdm.demos.tictactoe.ui.preferences.UserPreferencesScreenTag
import isel.pdm.demos.tictactoe.utils.PreserveDefaultDependenciesNoActivity
import isel.pdm.demos.tictactoe.utils.SuspendingGate
import isel.pdm.demos.tictactoe.utils.createPreserveDefaultDependenciesComposeRuleNoActivity
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Rule
import org.junit.Test

class LobbyActivityTests {

    @get:Rule
    val testRule = createPreserveDefaultDependenciesComposeRuleNoActivity()

    private val testApplication by lazy {
        (testRule.activityRule as PreserveDefaultDependenciesNoActivity).testApplication
    }
    private val testUserInfo = UserInfo("test", "test")
    private val testIntent = LobbyActivity.createIntent(testApplication, testUserInfo)


    @Test
    fun initially_the_lobby_screen_is_displayed() {
        // Arrange
        ActivityScenario.launch<LobbyActivity>(testIntent).use {
            // Act
            // Assert
            testRule.onNodeWithTag(LobbyScreenTag).assertExists()
        }
    }

    @Test
    fun pressing_back_finishes_activity() {
        // Arrange
        ActivityScenario.launch<LobbyActivity>(testIntent).use { scenario ->
            // Act
            testRule.onNodeWithTag(NavigateBackTag).performClick()
            // Assert
            scenario.onActivity { activity ->
                testRule.waitUntil(
                    timeoutMillis = 1000,
                    condition = { activity.isFinishing }
                )
            }
        }
    }

    @Test
    fun pressing_preferences_navigates_to_preferences() {
        // Arrange
        ActivityScenario.launch<LobbyActivity>(testIntent).use {
            // Act
            testRule.onNodeWithTag(NavigateToPreferencesTag).performClick()
            testRule.waitForIdle()
            // Assert
            testRule.onNodeWithTag(UserPreferencesScreenTag).assertExists()
        }
    }

    @Test
    fun lobby_is_entered_when_activity_is_started() = runBlocking {
        // Arrange
        val enteredGate = SuspendingGate()
        testApplication.lobby = mockk {
            coEvery { enter(any()) } coAnswers { enteredGate.open(); emptyFlow() }
            coEvery { leave() } returns Unit
        }

        // Act
        ActivityScenario.launch<LobbyActivity>(testIntent).use {
            // Assert
            testRule.onNodeWithTag(LobbyScreenTag).assertExists()
            withTimeout(3000) { enteredGate.await() }
        }
    }

    @Test
    fun lobby_is_left_when_activity_is_stopped() = runBlocking {
        // Arrange
        val leftGate = SuspendingGate()
        testApplication.lobby = mockk {
            coEvery { enter(any()) } coAnswers { emptyFlow() }
            coEvery { leave() } coAnswers { leftGate.open() }
        }

        // Act
        ActivityScenario.launch<LobbyActivity>(testIntent).use { _ ->
            // Stopping the activity by launching another one.
            // Could no find a way to stop it directly.
            testRule.onNodeWithTag(LobbyScreenTag).assertExists()
            testRule.onNodeWithTag(NavigateToPreferencesTag).performClick()
            testRule.waitForIdle()
            // Assert
            testRule.onNodeWithTag(UserPreferencesScreenTag).assertExists()
            testRule.onNodeWithTag(LobbyScreenTag).assertDoesNotExist()
            withTimeout(3000) { leftGate.await() }
        }
    }

    @Test
    fun an_error_message_is_displayed_if_an_error_occurs_while_entering_the_lobby() {
        // Arrange
        val expectedException = Exception("test exception")
        testApplication.lobby = mockk {
            coEvery { enter(any()) } coAnswers { throw expectedException }
            coEvery { leave() } returns Unit
        }

        // Act
        ActivityScenario.launch<LobbyActivity>(testIntent).use {
            // Assert
            testRule.onNodeWithTag(ErrorAlertTestTag).assertExists()
        }
    }

    @Test
    fun dismissing_error_message_finishes_the_activity() {
        // Arrange
        val expectedException = Exception("test exception")
        testApplication.lobby = mockk {
            coEvery { enter(any()) } coAnswers { throw expectedException }
            coEvery { leave() } returns Unit
        }

        // Act
        ActivityScenario.launch<LobbyActivity>(testIntent).use {
            // Assert
            testRule.onNodeWithTag(ErrorAlertTestTag).assertExists()
            testRule.onNodeWithTag(ErrorAlertDismissButtonTestTag).performClick()
            it.onActivity { activity ->
                testRule.waitUntil(
                    timeoutMillis = 1000,
                    condition = { activity.isFinishing }
                )
            }
        }
    }

    @Test
    fun pressing_a_player_card_navigates_to_game_activity(): Unit = runBlocking {
        // Arrange
        val enteredGate = SuspendingGate()
        testApplication.lobby = mockk(relaxed = true) {
            val localPlayer = slot<PlayerInfo>()
            coEvery { enter(capture(localPlayer)) } returns flow {
                emit(RosterUpdated(
                    buildList {
                        add(localPlayer.captured)
                        addAll(otherTestPlayersInLobby)
                    }
                ))
                enteredGate.open()
            }
            coEvery { leave() } returns Unit
        }

        ActivityScenario.launch<LobbyActivity>(testIntent).use {
            // Act
            withTimeout(3000) { enteredGate.await() }
            testRule.onAllNodesWithTag(PlayerInfoViewTag).onFirst().performClick()
            testRule.waitForIdle()
            // Assert
            testRule.onNodeWithTag(GamePlayScreenTag).assertExists()
        }
    }

    @Test
    fun receiving_a_challenge_navigates_to_game_activity(): Unit = runBlocking {
        // Arrange
        val challengeGate = SuspendingGate()
        testApplication.lobby = mockk(relaxed = true) {
            val localPlayer = slot<PlayerInfo>()
            coEvery { enter(capture(localPlayer)) } returns flow {
                emit(RosterUpdated(
                    buildList {
                        add(localPlayer.captured)
                        addAll(otherTestPlayersInLobby)
                    }
                ))

                val challenge = Challenge(
                    challenger = otherTestPlayersInLobby.first(),
                    challenged = localPlayer.captured
                )
                emit(ChallengeReceived(challenge))
                challengeGate.open()
            }
            coEvery { leave() } returns Unit
        }

        // Act
        ActivityScenario.launch<LobbyActivity>(testIntent).use {
            // Assert
            withTimeout(3000) { challengeGate.await() }
            testRule.onNodeWithTag(GamePlayScreenTag).assertExists()
        }
    }
}