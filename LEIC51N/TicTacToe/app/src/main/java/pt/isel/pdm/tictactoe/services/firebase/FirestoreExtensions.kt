package pt.isel.pdm.tictactoe.services.firebase

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeout
import pt.isel.pdm.tictactoe.model.GameSession
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.time.Duration

suspend fun <T> DocumentReference.waitForDocumentToChange(
    timeout: Duration = Duration.INFINITE,
    predicate: (DocumentSnapshot?) -> T
): T {
    val doc = this

    //
    //  See how to imprive this timeout situation
    //
    return suspendCancellableCoroutine() { continuation ->
        var timeoutJob: Job? = null

        if (timeout != Duration.INFINITE) {
            timeoutJob = CoroutineScope(Dispatchers.IO).launch {
                delay(timeout)
                if (continuation.isCompleted == false)
                    continuation.cancel(CancellationException("Timeout"))
            }
        }

        var listener: ListenerRegistration? = null
        listener = doc.addSnapshotListener() { docSnapshot, err ->
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
                if (continuation.isCompleted) {
                    listener!!.remove()
                    timeoutJob?.cancel()
                }
            }
        }

        continuation.invokeOnCancellation {
            listener.remove()
        }
    }
}


class FirestoreExtensions {
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

        fun mapToGameSession(gameDoc: DocumentSnapshot): GameSession {
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


