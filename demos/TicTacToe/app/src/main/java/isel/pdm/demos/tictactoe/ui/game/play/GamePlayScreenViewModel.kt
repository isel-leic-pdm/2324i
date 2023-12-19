package isel.pdm.demos.tictactoe.ui.game.play

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import isel.pdm.demos.tictactoe.domain.game.lobby.Challenge
import isel.pdm.demos.tictactoe.domain.game.lobby.PlayerInfo
import isel.pdm.demos.tictactoe.domain.game.lobby.getMarkerFor
import isel.pdm.demos.tictactoe.domain.game.play.Board
import isel.pdm.demos.tictactoe.domain.game.play.Coordinate
import isel.pdm.demos.tictactoe.domain.game.play.Game
import isel.pdm.demos.tictactoe.domain.game.play.Marker
import isel.pdm.demos.tictactoe.domain.game.play.Match
import isel.pdm.demos.tictactoe.domain.game.play.MatchEvent
import isel.pdm.demos.tictactoe.domain.game.play.isLocalPlayerTurn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * The state of the screen used to play the game.
 */
interface GamePlayScreenState {

    /**
     * The idle state, before the match starts.
     */
    data object Idle : GamePlayScreenState

    /**
     * The state while the match is starting. This is the screen's state while each
     * player is waiting for the opponent to join the match.
     * @property localPlayerMarker The marker of the local player.
     * @property challenge The challenge that originated the match.
     */
    data class Starting(val localPlayerMarker: Marker, val challenge: Challenge) : GamePlayScreenState

    /**
     * The state while the match is ongoing.
     * @property localPlayerMarker The marker of the local player.
     * @property game The game state while it is ongoing.
     */
    data class Started(val localPlayerMarker: Marker, val game: Game.Ongoing) : GamePlayScreenState

    /**
     * The state when the match is over.
     * @property localPlayerMarker The marker of the local player.
     * @property game The game state when it has ended.
     */
    data class Finished(val localPlayerMarker: Marker, val game: Game.Finished) : GamePlayScreenState

    /**
     * Gets the game board. If the game is not ongoing, returns an empty board.
     */
    fun getGameBoard(): Board = when (this) {
        is Started -> game.board
        is Finished -> game.board
        else -> Board.EMPTY
    }

    /**
     * Checks whether is the local player turn.
     */
    fun isLocalPlayerTurn(): Boolean = when (this) {
        is Started -> game.isLocalPlayerTurn(localPlayerMarker)
        else -> false
    }
}

/**
 * View model for the Game Play Screen hosted by [GamePlayActivity].
 * @param match the match to be played.
 */
class GamePlayScreenViewModel(private val match: Match) : ViewModel() {

    companion object {
        fun factory(match: Match) = viewModelFactory {
            initializer { GamePlayScreenViewModel(match) }
        }
    }

    private val _screenStateFlow: MutableStateFlow<GamePlayScreenState> =
        MutableStateFlow(GamePlayScreenState.Idle)

    /**
     * The flow of states the view model traverses.
     */
    val screenState: Flow<GamePlayScreenState>
        get() = _screenStateFlow.asStateFlow()

    /**
     * Starts the match.
     * @param localPlayer The local player info.
     * @param challenge The challenge that originated the match.
     * @return a flow of match screen states.
     * @throws IllegalStateException if a match has already been started.
     */
    fun startMatch(localPlayer: PlayerInfo, challenge: Challenge): Flow<GamePlayScreenState> {
        check(_screenStateFlow.value is GamePlayScreenState.Idle)
        _screenStateFlow.value = GamePlayScreenState.Starting(
            localPlayerMarker = challenge.getMarkerFor(localPlayer),
            challenge = challenge
        )
        viewModelScope.launch {
            match.start(localPlayer, challenge).collect { matchEvent ->
                val localMarker = challenge.getMarkerFor(localPlayer)
                _screenStateFlow.value = when (matchEvent) {
                    is MatchEvent.Started -> GamePlayScreenState.Started(
                        localPlayerMarker = localMarker,
                        game = matchEvent.game
                    )
                    is MatchEvent.MoveMade -> GamePlayScreenState.Started(
                        localPlayerMarker = localMarker,
                        game = matchEvent.game
                    )
                    is MatchEvent.Ended -> GamePlayScreenState.Finished(
                        localPlayerMarker = localMarker,
                        game = matchEvent.game
                    )
                }
            }
        }
        return screenState
    }

    /**
     * Makes a move on the current match.
     * @param at The coordinates where the move is to be made.
     * @throws IllegalStateException if a match has not been started.
     */
    fun makeMove(at: Coordinate) {
        check(_screenStateFlow.value is GamePlayScreenState.Started)
        viewModelScope.launch {
            match.makeMove(at)
        }
    }

    /**
     * Forfeits the current match.
     * @throws IllegalStateException if a match has not been started.
     */
    fun forfeit() {
        check(_screenStateFlow.value is GamePlayScreenState.Started)
        viewModelScope.launch { match.forfeit() }
    }
}