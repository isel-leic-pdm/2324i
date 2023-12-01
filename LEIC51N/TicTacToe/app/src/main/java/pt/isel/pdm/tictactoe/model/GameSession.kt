package pt.isel.pdm.tictactoe.model

interface GameSession {
    val player1: String
    val player2: String
    val isPlayer1Turn: Boolean
    val gameId: String
    val board: String

    companion object{
        const val Player1Move = "1"
        const val Player2Move = "2"
    }
}

