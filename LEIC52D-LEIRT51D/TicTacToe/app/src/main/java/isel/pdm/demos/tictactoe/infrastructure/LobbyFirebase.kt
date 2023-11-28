package isel.pdm.demos.tictactoe.infrastructure

import isel.pdm.demos.tictactoe.domain.game.Challenge
import isel.pdm.demos.tictactoe.domain.game.Lobby
import isel.pdm.demos.tictactoe.domain.game.LobbyEvent
import isel.pdm.demos.tictactoe.domain.game.PlayerInfo
import kotlinx.coroutines.flow.Flow

/**
 * Implementation of the Game's lobby using Firebase's Firestore.
 */
class LobbyFirebase() : Lobby {

    override suspend fun enter(localPlayer: PlayerInfo): Flow<LobbyEvent> {
        TODO("Not yet implemented")
    }

    override suspend fun issueChallenge(to: PlayerInfo): Challenge {
        TODO("Not yet implemented")
    }

    override suspend fun leave() {
        TODO("Not yet implemented")
    }
}
