package isel.pdm.demos.tictactoe.domain.game.lobby

import isel.pdm.demos.tictactoe.domain.game.play.Marker

/**
 * Data type that characterizes challenges.
 * @property challenger     The challenger information
 * @property challenged     The information of the challenged player
 */
data class Challenge(val challenger: PlayerInfo, val challenged: PlayerInfo)

/**
 * The player information of the first player to move for this challenge.
 */
val Challenge.firstToMove: PlayerInfo
    get() = challenger

val Challenge.secondToMove: PlayerInfo
    get() = challenged

/**
 * Function used to computes the player marker for the given challenge.
 * @param player The player information
 * @return the player marker
 * @throws IllegalArgumentException if the player is not part of the challenge
 */
fun Challenge.getMarkerFor(player: PlayerInfo): Marker =
    when (player) {
        firstToMove -> Marker.firstToMove
        secondToMove -> Marker.firstToMove.other
        else -> throw IllegalArgumentException("Player is not part of the challenge")
    }
