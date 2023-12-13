package isel.pdm.demos.tictactoe.domain.game.play

import org.junit.Assert
import org.junit.Test

class BoardTests {

    @Test
    fun `newly created board has no moves`() {
        val sut = Board()
        Assert.assertTrue(sut.flatten().all { it == null })
    }

    @Test
    fun `making a move on an empty tile succeeds`() {
        // Arrange
        val at = Coordinate(row = 1, column = 2)
        val sut = Board()

        // Act
        val boardAfterMove = sut.placeMarker(Marker.firstToMove, at)

        // Assert
        val move = boardAfterMove.getMove(at)
        Assert.assertNotNull(move)
    }

    @Test(expected = IllegalStateException::class)
    fun `making a move on a non empty tile throws`() {
        // Arrange
        val at = Coordinate(row = 0, column = 0)
        val sut = Board().placeMarker(Marker.firstToMove, at)

        // Act
        sut.placeMarker(Marker.firstToMove.other, at)
    }

    @Test
    fun `indexing the board returns the correct marker`() {
        // Arrange
        val expectedMarker = Marker.CROSS
        val sut = Board(
            tiles = listOf(
                listOf(null, expectedMarker, null),
                List(size = BOARD_SIDE) { null },
                List(size = BOARD_SIDE) { null }
            )
        )

        // Act
        val actualMarker = sut[Coordinate(row = 0, column = 1)]

        // Assert
        Assert.assertEquals(expectedMarker, actualMarker)
    }

    @Test
    fun `flatten returns the correct list of moves`() {
        // Arrange
        val expectedMoves = listOf(
            Marker.CROSS, null, null,
            null, Marker.CIRCLE, null,
            null, null, null
        )
        val sut = Board(
            tiles = listOf(
                listOf(Marker.CROSS, null, null),
                listOf(null, Marker.CIRCLE, null),
                List(size = BOARD_SIDE) { null }
            )
        )

        // Act
        val actualMoves = sut.flatten()

        // Assert
        Assert.assertEquals(expectedMoves, actualMoves)
    }

    @Test
    fun `getRows returns the correct list of rows`() {
        // Arrange
        val expectedRows = listOf(
            listOf(Marker.CROSS, Marker.CIRCLE, null),
            listOf(null, Marker.CIRCLE, null),
            List(size = BOARD_SIDE) { null }
        )
        val sut = Board(
            tiles = listOf(
                listOf(Marker.CROSS, Marker.CIRCLE, null),
                listOf(null, Marker.CIRCLE, null),
                List(size = BOARD_SIDE) { null }
            )
        )

        // Act
        val actualRows = sut.getRows()

        // Assert
        Assert.assertEquals(expectedRows, actualRows)
    }

    @Test
    fun `getColumns returns the correct list of columns`() {
        // Arrange
        val expectedColumns = listOf(
            listOf(Marker.CROSS, Marker.CIRCLE, null),
            listOf(null, Marker.CIRCLE, null),
            List(size = BOARD_SIDE) { null }
        )
        val sut = Board(
            tiles = listOf(
                listOf(Marker.CROSS, null, null),
                listOf(Marker.CIRCLE, Marker.CIRCLE, null),
                List(size = BOARD_SIDE) { null }
            )
        )

        // Act
        val actualColumns = sut.getColumns()

        // Assert
        Assert.assertEquals(expectedColumns, actualColumns)
    }

    @Test
    fun `getDiagonals returns the correct list of diagonals`() {
        // Arrange
        val expectedDiagonals = Pair(
            listOf(Marker.CROSS, Marker.CIRCLE, null),
            listOf(null, Marker.CIRCLE, null)
        )
        val sut = Board(
            tiles = listOf(
                listOf(Marker.CROSS, null, null),
                listOf(null, Marker.CIRCLE, null),
                List(size = BOARD_SIDE) { null }
            )
        )

        // Act
        val actualDiagonals = sut.getDiagonals()

        // Assert
        Assert.assertEquals(expectedDiagonals, actualDiagonals)
    }
}