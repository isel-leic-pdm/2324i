package pt.isel.pdm.tictactoe.services

import pt.isel.pdm.tictactoe.model.GameLobby
import pt.isel.pdm.tictactoe.model.GameSession

interface MatchmakingService {
    suspend fun getLobbies(): List<GameLobby>
    suspend fun createLobbyAndWaitForPlayer(userName: String): GameSession
    suspend fun joinLobby(userName: String, lobby: GameLobby): GameSession
}

