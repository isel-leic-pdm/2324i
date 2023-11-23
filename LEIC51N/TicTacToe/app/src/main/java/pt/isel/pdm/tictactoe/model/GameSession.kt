package pt.isel.pdm.tictactoe.model

interface GameSession {
    val player1 :String
    val player2: String
    val isPlayer1Turn : Boolean
    val gameId : String
}