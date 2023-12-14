package isel.pdm.demos.tictactoe.domain.game.lobby

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
