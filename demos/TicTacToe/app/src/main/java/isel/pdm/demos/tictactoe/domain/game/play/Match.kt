package isel.pdm.demos.tictactoe.domain.game.play

import isel.pdm.demos.tictactoe.domain.game.lobby.Challenge
import isel.pdm.demos.tictactoe.domain.game.lobby.PlayerInfo
import kotlinx.coroutines.flow.Flow

/**
 * Sum type used to describe events occurring while the match is ongoing.
 */
sealed class MatchEvent(val game: Game) {
    /**
     * Signals that the game has started.
     */
    class Started(game: Game) : MatchEvent(game)

    /**
     * Signals that the a move was made.
     */
    class MoveMade(game: Game) : MatchEvent(game)

    /**
     * Signals the game termination.
     * @property [winner] the marker of the winner, if any. If the game is tied
     * this property is null.
     */
    class Ended(game: Game, val winner: Marker? = null) : MatchEvent(game)
}

/**
 * Abstraction that characterizes a match between two players, that is, the
 * required interactions.
 */
interface Match {

    /**
     * The local player marker
     */
    val localPlayer: Marker

    /**
     * Starts the match. The first to make a move is the challenger. The game
     * is only actually in progress after its initial state is published on the flow.
     * @param [localPlayer] the local player information
     * @param [challenge] the challenge bearing the players' information
     * @return the flow of game state change events, expressed as [MatchEvent] instances
     * @throws IllegalStateException if a game is in progress
     */
    fun start(localPlayer: PlayerInfo, challenge: Challenge): Flow<MatchEvent>

    /**
     * Makes a move at the given coordinates.
     * @throws IllegalStateException if a game is not in progress or the move is illegal,
     * either because it's not the local player turn or the position is not free.
     */
    suspend fun makeMove(at: Coordinate)

    /**
     * Forfeits the current game.
     * @throws IllegalStateException if a game is not in progress
     */
    suspend fun forfeit()

    /**
     * Ends the match, cleaning up if necessary.
     */
    suspend fun end()
}
