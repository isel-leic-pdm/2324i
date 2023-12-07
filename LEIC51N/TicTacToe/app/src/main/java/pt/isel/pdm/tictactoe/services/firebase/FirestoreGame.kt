package pt.isel.pdm.tictactoe.services.firebase

import pt.isel.pdm.tictactoe.model.GameSession

class FirestoreGame(
    override val player1: String,
    override val player2: String,
    override val isPlayer1Turn: Boolean,
    override val gameId: String, override val board: String = ""
) : GameSession {
    override fun toString(): String {
        return "FirestoreGame(player1='$player1', player2='$player2', isPlayer1Turn=$isPlayer1Turn, gameId='$gameId', board='$board' )"
    }
}