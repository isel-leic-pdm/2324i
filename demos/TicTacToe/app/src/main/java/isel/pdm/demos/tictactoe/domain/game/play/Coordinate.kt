package isel.pdm.demos.tictactoe.domain.game.play

/**
 * The Tic-Tac-Toe's board side
 */
const val BOARD_SIDE = 3

/**
 * Represents coordinates in the Tic-Tac-Toe board
 * @property row     the row index
 * @property column  the column index
 */
data class Coordinate(val row: Int, val column: Int) {
    init {
        require(isValidRow(row) && isValidColumn(column))
    }

    companion object {
        /**
         * Creates a [Coordinate] instance from the given index.
         * @param index the index in the range [0, BOARD_SIDE * BOARD_SIDE - 1]
         * @return the [Coordinate] instance
         */
        fun fromIndex(index: Int) = Coordinate(index / BOARD_SIDE, index % BOARD_SIDE)
    }

    /**
     * Converts this coordinate to an index.
     * @return the index in the range [0, BOARD_SIDE * BOARD_SIDE - 1]
     */
    fun toIndex() = row * BOARD_SIDE + column
}

/**
 * Checks whether [value] is a valid row index
 */
fun isValidRow(value: Int) = value in 0 until BOARD_SIDE

/**
 * Checks whether [value] is a valid column index
 */
fun isValidColumn(value: Int) = value in 0 until BOARD_SIDE

