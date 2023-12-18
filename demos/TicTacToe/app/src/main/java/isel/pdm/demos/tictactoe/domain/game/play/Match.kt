package isel.pdm.demos.tictactoe.domain.game.play

import isel.pdm.demos.tictactoe.domain.game.lobby.Challenge
import isel.pdm.demos.tictactoe.domain.game.lobby.PlayerInfo
import kotlinx.coroutines.flow.Flow

/**
 * Sum type used to describe events occurring while the match is ongoing.
 */
sealed interface MatchEvent {

    /**
     * The game state at the time of the event.
     */
    val game: Game

    /**
     * Signals that the game has started.
     */
    data class Started(override val game: Game.Ongoing) : MatchEvent

    /**
     * Signals that the a move was made.
     */
    data class MoveMade(override val game: Game.Ongoing) : MatchEvent

    /**
     * Signals the game termination.
     * @property [winner] the marker of the winner, if any. If the game is tied
     * this property is null.
     */
    data class Ended(override val game: Game.Finished, val winner: Marker? = null) : MatchEvent
}

/**
 * Abstraction that characterizes a match between two players, that is, the
 * required interactions. When the match is over, it must be closed.
 */
interface Match {

    /**
     * The local player marker, or null if the match has not started.
     */
    val localPlayerMarker: Marker?

    /**
     * Starts the match. The first to make a move is the challenger. The game
     * is only actually in progress after its initial state is published on the flow.
     * @param [localPlayer] the local player information
     * @param [challenge] the challenge bearing the players' information
     * @return the flow of game state change events, expressed as [MatchEvent] instances
     * @throws IllegalStateException if a game is in progress or has already ended.
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
     * Closes the match, cleaning up if necessary. If there's a game in progress,
     * it's forfeited for the local player.
     */
    suspend fun close()
}

