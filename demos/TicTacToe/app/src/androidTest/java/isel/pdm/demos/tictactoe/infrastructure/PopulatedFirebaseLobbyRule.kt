package isel.pdm.demos.tictactoe.infrastructure

import androidx.test.platform.app.InstrumentationRegistry
import isel.pdm.demos.tictactoe.TicTacToeTestApplication
import isel.pdm.demos.tictactoe.domain.game.lobby.Challenge
import isel.pdm.demos.tictactoe.domain.game.lobby.Lobby
import isel.pdm.demos.tictactoe.domain.game.lobby.PlayerInfo
import isel.pdm.demos.tictactoe.domain.user.UserInfo
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement


/**
 * Test rule that ensures that the lobby is populated with the test data and
 * that it is emptied after the test is executed.
 */
class PopulatedFirebaseLobbyRule : TestRule {

    private val testApp: TicTacToeTestApplication by lazy {
        InstrumentationRegistry
            .getInstrumentation()
            .targetContext
            .applicationContext as TicTacToeTestApplication
    }

    val lobby: Lobby = LobbyFirebase(testApp.emulatedFirestoreDb)

    suspend fun addPlayerToLobby(player: PlayerInfo) {
        testApp.emulatedFirestoreDb
            .collection(LOBBY)
            .document(player.id.toString())
            .set(player.toDocumentContent())
            .await()
    }

    suspend fun getChallengeInfo(challengedPlayer: PlayerInfo): Challenge? =
        testApp.emulatedFirestoreDb
            .collection(LOBBY)
            .document(challengedPlayer.id.toString())
            .get()
            .await()
            .toChallengeOrNull()

    suspend fun updateChallengerInfo(challengedPlayer: PlayerInfo, challenger: PlayerInfo) {
        testApp.emulatedFirestoreDb
            .collection(LOBBY)
            .document(challengedPlayer.id.toString())
            .update(CHALLENGER_FIELD, challenger.toDocumentContent())
            .await()
    }

    private suspend fun populateLobby() {
        otherTestPlayersInLobby.forEach {
            addPlayerToLobby(it)
        }
    }

    private suspend fun emptyLobby() {
        testApp.emulatedFirestoreDb
            .collection(LOBBY)
            .get()
            .await()
            .forEach {
                testApp.emulatedFirestoreDb
                    .collection(LOBBY)
                    .document(it.id)
                    .delete()
                    .await()
            }
    }

    override fun apply(test: Statement, description: Description): Statement =
        object : Statement() {
            override fun evaluate() = runBlocking {
                try {
                    populateLobby()
                    test.evaluate()
                }
                finally {
                    emptyLobby()
                }
            }
        }
}

// Test data

val localTestPlayer = PlayerInfo(UserInfo("local"))

val otherTestPlayersInLobby: List<PlayerInfo> = buildList {
    repeat(3) {
        add(PlayerInfo(UserInfo("remote $it", "motto")))
    }
}
