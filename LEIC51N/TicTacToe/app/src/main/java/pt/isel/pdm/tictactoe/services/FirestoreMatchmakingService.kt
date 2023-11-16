package pt.isel.pdm.tictactoe.services

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import pt.isel.pdm.tictactoe.model.GameLobby
import pt.isel.pdm.tictactoe.model.GameSession
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FirestoreMatchmakingService(
    private val db: FirebaseFirestore
) : MatchmakingService {

    companion object {
        const val LobbyCollection = "lobbies"

        const val LobbyDisplayNameField = "displayName"
        const val LobbyGameIdField = "gameId"
    }

    override suspend fun getLobbies(): List<GameLobby> {
        return db.collection(LobbyCollection)
            .get()
            .await()
            .map {
                FirestoreLobby(
                    displayName = it.getString(LobbyDisplayNameField)!!,
                    gameId = it.getString(LobbyGameIdField) ?: ""
                )
            }

    }

    override suspend fun createLobbyAndWaitForPlayer(userName: String): GameSession {
        //criar lobby
        val lobbyDoc = db.collection(LobbyCollection).add(
            hashMapOf(
                LobbyDisplayNameField to userName
            )
        ).await()

        //esperar pelo gameid
        val gameId = suspendCancellableCoroutine { continuation ->

            var listener: ListenerRegistration? = null
            listener = lobbyDoc.addSnapshotListener { lobbySnapshot, err ->
                try {

                    if (err != null)
                        continuation.resumeWithException(Exception(err.message))
                    else if (lobbySnapshot == null)
                        continuation.resumeWithException(Exception("Lobby not found"))
                    else {
                        val gameId = lobbySnapshot.getString(LobbyGameIdField)

                        if (gameId.isNullOrEmpty() == false)
                            continuation.resume(gameId)
                    }

                } finally {
                    if (continuation.isCompleted)
                        listener!!.remove()
                }
            }

            continuation.invokeOnCancellation {
                listener.remove()
            }
        }

        //criar jogo

        //apagar lobby
        //esperar por P2 name
    }

    override suspend fun joinLobby(userName: String, lobby: GameLobby): GameSession {
        TODO("Not yet implemented")
    }


    class FirestoreLobby(
        override val displayName: String,
        override val gameId: String
    ) : GameLobby {
        override fun toString(): String {
            return "FirestoreLobby(displayName='$displayName', gameId='$gameId')"
        }
    }
}