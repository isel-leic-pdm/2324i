package isel.pdm.demos.tictactoe.infrastructure

import androidx.test.platform.app.InstrumentationRegistry
import isel.pdm.demos.tictactoe.TicTacToeTestApplication
import isel.pdm.demos.tictactoe.domain.game.lobby.Challenge
import isel.pdm.demos.tictactoe.domain.game.play.Match
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class CleanupFirebaseMatchRule : TestRule {

    val testApp: TicTacToeTestApplication by lazy {
        InstrumentationRegistry
            .getInstrumentation()
            .targetContext
            .applicationContext as TicTacToeTestApplication
    }

    val remoteInitiatedChallenge = Challenge(
        challenger = otherTestPlayersInLobby.first(),
        challenged = localTestPlayer
    )

    val locallyInitiatedChallenge = Challenge(
        challenger = localTestPlayer,
        challenged = otherTestPlayersInLobby.first(),
    )

    val match: Match = MatchFirebase(testApp.emulatedFirestoreDb)

    private suspend fun cleanup() {
        testApp.emulatedFirestoreDb
            .collection(ONGOING)
            .get()
            .await()
            .map { it.id }
            .forEach {
                testApp.emulatedFirestoreDb
                    .collection(ONGOING)
                    .document(it)
                    .delete()
                    .await()
            }
    }

    override fun apply(test: Statement, description: Description): Statement =
        object : Statement() {
            override fun evaluate() = runBlocking {
                try {
                    test.evaluate()
                }
                finally {
                    cleanup()
                }
            }
        }
}