package pt.isel.pdm.tictactoe.services

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import pt.isel.pdm.tictactoe.model.GameLobby
import pt.isel.pdm.tictactoe.model.GameSession
import pt.isel.pdm.tictactoe.services.firebase.FirestoreExtensions
import pt.isel.pdm.tictactoe.services.firebase.FirestoreLobby
import pt.isel.pdm.tictactoe.services.firebase.waitForDocumentToChange
import java.util.Random
import java.util.UUID

class FirestoreMatchmakingService(
    private val db: FirebaseFirestore
) : MatchmakingService {




    override suspend fun getLobbies(): List<GameLobby> {
        return db.collection(FirestoreExtensions.LobbyCollection)
            .get()
            .await()
            .map {
                FirestoreLobby(
                    displayName = it.getString(FirestoreExtensions.LobbyDisplayNameField)!!,
                    id = it.id
                )
            }

    }

    override suspend fun createLobbyAndWaitForPlayer(userName: String): GameSession {

        var lobbyDoc: DocumentReference? = null
        var gameDoc: DocumentReference? = null
        try {
            //criar lobby
            lobbyDoc = db.collection(FirestoreExtensions.LobbyCollection).add(
                hashMapOf(
                    FirestoreExtensions.LobbyDisplayNameField to userName
                )
            ).await()

            //esperar pelo gameid

            val gameId = lobbyDoc.waitForDocumentToChange(

            ) { lobbySnapshot ->

                if (lobbySnapshot == null || lobbySnapshot.exists() == false)
                    throw Exception("Lobby not found")

                val gameId = lobbySnapshot.getString(FirestoreExtensions.LobbyGameIdField)

                if (gameId.isNullOrEmpty())
                    return@waitForDocumentToChange null
                else
                    return@waitForDocumentToChange gameId

            }
            //criar jogo
            gameDoc = db.collection(FirestoreExtensions.GamesCollection).document(gameId!!)

            gameDoc.set(
                hashMapOf(
                    FirestoreExtensions.GamePlayer1Field to userName,
                    FirestoreExtensions.GameIsPlayer1Turn to Random().nextBoolean(),
                    FirestoreExtensions.GameIdField to gameId,
                    FirestoreExtensions.GameBoardField to FirestoreExtensions.EmptyGameBoard

                )
            ).await()

            //apagar lobby
            lobbyDoc.delete().await()

            //esperar por P2 name
            gameDoc.waitForDocumentToChange() { gameSnapshot ->
                if (gameSnapshot == null || gameSnapshot.exists() == false)
                    throw Exception("Game $gameId not found")

                val player2 = gameSnapshot.getString(FirestoreExtensions.GamePlayer2Field)

                if (player2.isNullOrEmpty())
                    return@waitForDocumentToChange null
                else
                    return@waitForDocumentToChange Unit
            }

            //
            //  return game session
            //
            return FirestoreExtensions.mapToGameSession(gameDoc.get().await())
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

        val lobbyReference = db.collection(FirestoreExtensions.LobbyCollection)
            .document(lobby.id)

        lobbyReference.update(FirestoreExtensions.LobbyGameIdField, gameId)
            .await()

        lobbyReference.waitForDocumentToChange() { lobbySnapshot ->
            if (lobbySnapshot == null || lobbySnapshot.exists() == false)
                return@waitForDocumentToChange Unit

            return@waitForDocumentToChange null
        }


        //Player 2 waits for lobby to be destroyed
        lobbyReference.waitForDocumentToChange() { lobbySnapshot ->
            if (lobbySnapshot == null || lobbySnapshot.exists() == false)
                return@waitForDocumentToChange Unit

            return@waitForDocumentToChange null
        }

        //Player 2 checks if game with its id is created

        val gameRef = db.collection(FirestoreExtensions.GamesCollection)
            .document(gameId)

        val gameDoc = gameRef.get()
            .await()

        if (gameDoc == null || gameDoc.exists() == false)
            throw Exception("Failed to connect with a player")

        //Player 2 adds its player name and game starts
        gameRef.update(FirestoreExtensions.GamePlayer2Field, userName).await()

        return FirestoreExtensions.mapToGameSession(gameRef.get().await())
    }




}

