package isel.pdm.demos.tictactoe.domain.game.play


typealias BoardRow = List<Marker?>
typealias BoardColumn = List<Marker?>
typealias BoardDiagonal = List<Marker?>

/**
 * Represents a Tic-Tac-Toe board. Instances are immutable.
 * @property tiles  The board tiles
 */
data class Board(
    private val tiles: List<List<Marker?>> = List(BOARD_SIDE) { List(BOARD_SIDE) { null } }
) {
    companion object {
        val EMPTY = Board()

        fun fromMovesList(moves: List<Marker?>) = Board(
            tiles = List(size = BOARD_SIDE, init = { row ->
                List(size = BOARD_SIDE, init = { col ->
                    moves[row * BOARD_SIDE + col]
                })
            })
        )
    }

    /**
     * Overloads the indexing operator for getting the move at the given coordinates.
     * @param at    the board's coordinate
     * @return the [Marker] instance that made the move, or null if the position is empty
     * @throws IllegalArgumentException if the index is out of range
     */
    operator fun get(at: Coordinate): Marker? = getMove(at)

    /**
     * Gets the move at the given coordinates.
     * @param at    the move's coordinates
     * @return the [Marker] instance that made the move, or null if the position is empty
     */
    fun getMove(at: Coordinate): Marker? = tiles[at.row][at.column]

    /**
     * Places the marker at the given coordinate and returns the new board instance.
     * @param marker the marker to place
     * @param at    the board's coordinate
     * @throws IllegalArgumentException if the position is already occupied
     * @return the new board instance
     */
    fun placeMarker(marker: Marker, at: Coordinate): Board {
        require(this[at] == null)
        return copy(
            tiles = tiles.mapIndexed { row, elem ->
                if (row == at.row)
                    List(tiles[row].size) { col ->
                        if (col == at.column) marker
                        else tiles[row][col]
                    }
                else
                    elem
            }
        )
    }

    /**
     * Gets the list of moves in this board. The list is ordered by the moves' coordinates and
     * contains null values for empty tiles.
     * @return the list of moves
     */
    fun flatten(): List<Marker?> = tiles.flatten()

    /**
     * Gets the list of rows in this board. The list is ordered by the rows' indexes and
     * contains null values for empty tiles.
     * @return the list of rows
     */
    fun getRows(): List<BoardRow> = tiles

    /**
     * Gets the list of columns in this board. The list is ordered by the columns' indexes and
     * contains null values for empty tiles.
     * @return the list of columns
     */
    fun getColumns(): List<BoardColumn> = List(tiles.size) { col -> tiles.map { it[col] } }

    /**
     * Gets the board's diagonals. The first element of the pair is the diagonal from top-left to
     * bottom-right, and the second element is the diagonal from bottom-left to top-right.
     * @return the pair of diagonals
     */
    fun getDiagonals(): Pair<BoardDiagonal, BoardDiagonal> = Pair(
        first = List(BOARD_SIDE) { i -> tiles[i][i] },
        second = List(BOARD_SIDE) { i -> tiles[i][BOARD_SIDE - i - 1] }
    )

    /**
     * Checks whether the board is empty or not.
     * @return true if the board is empty, false otherwise
     */
    fun isEmpty(): Boolean = tiles.all { row -> row.all { it == null } }
}

