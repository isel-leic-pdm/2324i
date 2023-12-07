package pt.isel.pdm.tictactoe.services.firebase

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.suspendCancellableCoroutine
import pt.isel.pdm.tictactoe.model.GameSession
import pt.isel.pdm.tictactoe.services.FirestoreMatchmakingService
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

suspend fun <T> DocumentReference.waitForDocumentToChange(predicate: (DocumentSnapshot?) -> T): T {
    val doc = this
    return suspendCancellableCoroutine { continuation ->
        var listener: ListenerRegistration? = null
        listener = doc.addSnapshotListener { docSnapshot, err ->
            try {
                if (err != null)
                    continuation.resumeWithException(Exception(err.message))
                else {
                    val ret = predicate(docSnapshot)
                    if (ret != null)
                        continuation.resume(ret)
                }

            } catch (e: Exception) {
                continuation.resumeWithException(e)
            } finally {
                if (continuation.isCompleted)
                    listener!!.remove()
            }
        }

        continuation.invokeOnCancellation {
            listener.remove()
        }
    }
}

sealed class FirestoreExtensions {
    companion object {

        const val LobbyCollection = "lobbies"

        const val LobbyDisplayNameField = "displayName"
        const val LobbyGameIdField = "gameId"


        const val GamesCollection = "games"
        const val GamePlayer1Field = "player1"
        const val GamePlayer2Field = "player2"
        const val GameIsPlayer1Turn = "isPlayer1Turn"
        const val GameIdField = "gameId"
        const val GameBoardField = "board"

        const val EmptyGameBoard = "         "

        fun createGameSession(gameDoc: DocumentSnapshot): GameSession {
            return FirestoreGame(
                player1 = gameDoc.getString(GamePlayer1Field)!!,
                player2 = gameDoc.getString(GamePlayer2Field)!!,
                isPlayer1Turn = gameDoc.getBoolean(GameIsPlayer1Turn)!!,
                gameId = gameDoc.getString(GameIdField)!!,
                board = gameDoc.getString(GameBoardField)!!
            )
        }
    }
}


