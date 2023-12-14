package isel.pdm.demos.tictactoe.domain.game.play

/**
 * Enumeration type used to represent the game's moves.
 */
enum class Marker {

    CIRCLE, CROSS;

    /**
     * The player that starts the game, if no starting player is specified
     */
    companion object {
        val firstToMove: Marker = CIRCLE
    }

    /**
     * The other marker
     */
    val other: Marker
        get() = if (this == CIRCLE) CROSS else CIRCLE
}

