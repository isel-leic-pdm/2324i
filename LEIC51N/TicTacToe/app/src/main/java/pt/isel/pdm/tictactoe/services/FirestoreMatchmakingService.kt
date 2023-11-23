package pt.isel.pdm.tictactoe.services

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import pt.isel.pdm.tictactoe.model.GameLobby
import pt.isel.pdm.tictactoe.model.GameSession
import java.util.Random
import java.util.UUID
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FirestoreMatchmakingService(
    private val db: FirebaseFirestore
) : MatchmakingService {

    companion object {
        const val LobbyCollection = "lobbies"

        const val LobbyDisplayNameField = "displayName"
        const val LobbyGameIdField = "gameId"


        const val GamesCollection = "games"
        const val GamePlayer1Field = "player1"
        const val GamePlayer2Field = "player2"
        const val GameIsPlayer1Turn = "isPlayer1Turn"
        const val GameIdField = "gameId"

    }


    override suspend fun getLobbies(): List<GameLobby> {
        return db.collection(LobbyCollection)
            .get()
            .await()
            .map {
                FirestoreLobby(
                    displayName = it.getString(LobbyDisplayNameField)!!,
                    id = it.id
                )
            }

    }

    override suspend fun createLobbyAndWaitForPlayer(userName: String): GameSession {

        var lobbyDoc: DocumentReference? = null
        var gameDoc: DocumentReference? = null
        try {
            //criar lobby
            lobbyDoc = db.collection(LobbyCollection).add(
                hashMapOf(
                    LobbyDisplayNameField to userName
                )
            ).await()

            //esperar pelo gameid

            val gameId = waitForDocumentToChange(
                lobbyDoc
            ) { lobbySnapshot ->

                if (lobbySnapshot == null || lobbySnapshot.exists() == false)
                    throw Exception("Lobby not found")

                val gameId = lobbySnapshot.getString(LobbyGameIdField)

                if (gameId.isNullOrEmpty())
                    return@waitForDocumentToChange null
                else
                    return@waitForDocumentToChange gameId

            }
            //criar jogo
            gameDoc = db.collection(GamesCollection).document(gameId!!)

            gameDoc.set(
                hashMapOf(
                    GamePlayer1Field to userName,
                    GameIsPlayer1Turn to Random().nextBoolean(),
                    GameIdField to gameId,
                )
            ).await()

            //apagar lobby
            lobbyDoc.delete().await()

            //esperar por P2 name
            waitForDocumentToChange(gameDoc) { gameSnapshot ->
                if (gameSnapshot == null || gameSnapshot.exists() == false)
                    throw Exception("Game $gameId not found")

                val player2 = gameSnapshot.getString(GamePlayer2Field)

                if (player2.isNullOrEmpty())
                    return@waitForDocumentToChange null
                else
                    return@waitForDocumentToChange Unit
            }

            //
            //  return game session
            //
            return createGameSession(gameDoc.get().await())
        } catch (e: Exception) {
            if (lobbyDoc != null)
                lobbyDoc.delete().await()

            if (gameDoc != null)
                gameDoc.delete().await()

            throw e
        }

    }


    override suspend fun joinLobby(userName: String, lobby: GameLobby): GameSession {

        //Player 2 sets unique name on lobby
        val gameId = userName + UUID.randomUUID().toString()

        val lobbyReference = db.collection(LobbyCollection)
            .document(lobby.id)

        lobbyReference.update(LobbyGameIdField, gameId)
            .await()

        //Player 2 waits for lobby to be destroyed
        waitForDocumentToChange(lobbyReference) { lobbySnapshot ->
            if (lobbySnapshot == null || lobbySnapshot.exists() == false)
                return@waitForDocumentToChange Unit

            return@waitForDocumentToChange null
        }

        //Player 2 checks if game with its id is created

        val gameRef = db.collection(GamesCollection)
            .document(gameId)

        val gameDoc = gameRef.get()
            .await()

        if (gameDoc == null || gameDoc.exists() == false)
            throw Exception("Failed to connect with a player")

        //Player 2 adds its player name and game starts
        gameRef.update(GamePlayer2Field, userName).await()

        return createGameSession(gameRef.get().await())
    }

    private fun createGameSession(gameDoc: DocumentSnapshot): GameSession {
        return FirestoreGame(
            player1 = gameDoc.getString(GamePlayer1Field)!!,
            player2 = gameDoc.getString(GamePlayer2Field)!!,
            isPlayer1Turn = gameDoc.getBoolean(GameIsPlayer1Turn)!!,
            gameId = gameDoc.getString(GameIdField)!!
        )
    }


    suspend fun <T> waitForDocumentToChange(
        doc: DocumentReference,
        predicate: (DocumentSnapshot?) -> T
    ): T {
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


    class FirestoreLobby(
        override val displayName: String,
        override val id: String
    ) : GameLobby {
        override fun toString(): String {
            return "FirestoreLobby(displayName='$displayName', gameId='$id')"
        }
    }

    class FirestoreGame(
        override val player1: String,
        override val player2: String,
        override val isPlayer1Turn: Boolean,
        override val gameId: String
    ) : GameSession {
        override fun toString(): String {
            return "FirestoreGame(player1='$player1', player2='$player2', isPlayer1Turn=$isPlayer1Turn, gameId='$gameId')"
        }
    }

}