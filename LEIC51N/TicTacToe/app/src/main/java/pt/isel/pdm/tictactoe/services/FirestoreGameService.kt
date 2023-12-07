package pt.isel.pdm.tictactoe.services

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import pt.isel.pdm.tictactoe.model.GameInfo
import pt.isel.pdm.tictactoe.model.GameSession
import pt.isel.pdm.tictactoe.services.firebase.FirestoreExtensions
import pt.isel.pdm.tictactoe.services.firebase.waitForDocumentToChange
import java.io.InvalidObjectException
import java.lang.StringBuilder

class FirestoreGameService(
    private val db: FirebaseFirestore
) : GameService {


    override suspend fun getGameSession(info: GameInfo): GameSession {
        return getGameSession(info.gameId)
    }


    override suspend fun play(game: GameSession, idx: Int, isPlayer1: Boolean): GameSession {
        val board = StringBuilder(game.board)
        val move = if (isPlayer1) GameService.Player1Move else GameService.Player2Move
        board.setCharAt(idx, move)

        getGameDocument(game.gameId).update(
            hashMapOf(
                FirestoreExtensions.GameBoardField to board.toString(),
                FirestoreExtensions.GameIsPlayer1Turn to !game.isPlayer1Turn
            ) as Map<String, Any>
        ).await()

        return getGameSession(game.gameId)
    }

    override suspend fun waitForOtherPlayer(game: GameSession): GameSession {
        return getGameDocument(game.gameId).waitForDocumentToChange { gameDoc ->
            if (gameDoc == null || gameDoc.exists() == false)
                throw InvalidObjectException("Game object no longer exists")

            if (gameDoc.getBoolean(FirestoreExtensions.GameIsPlayer1Turn) == game.isPlayer1Turn)
                return@waitForDocumentToChange null

            return@waitForDocumentToChange FirestoreExtensions.mapToGameSession(gameDoc)
        }!!
    }

    private fun getGameDocument(gameId: String): DocumentReference {
        return db.collection(FirestoreExtensions.GamesCollection)
            .document(gameId)
    }

    private suspend fun getGameSession(gameId: String): GameSession {
        val gamedoc =
            getGameDocument(gameId)
                .get()
                .await()

        return FirestoreExtensions.mapToGameSession(gamedoc)
    }

}