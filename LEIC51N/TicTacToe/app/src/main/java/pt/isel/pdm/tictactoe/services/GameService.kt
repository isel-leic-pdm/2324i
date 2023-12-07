package pt.isel.pdm.tictactoe.services

import pt.isel.pdm.tictactoe.model.GameInfo
import pt.isel.pdm.tictactoe.model.GameSession

interface GameService {
    companion object{
        const val Player1Move = '1'
        const val Player2Move = '2'
    }
    suspend fun getGameSession(info : GameInfo): GameSession
    suspend fun play(game: GameSession, idx: Int, isPlayer1: Boolean) : GameSession
    suspend fun waitForOtherPlayer(game: GameSession) : GameSession
}