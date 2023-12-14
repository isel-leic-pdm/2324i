package isel.pdm.demos.tictactoe.domain.game.play

import isel.pdm.demos.tictactoe.utils.xAssertIs
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Test

class GameTests {

    @Test
    fun `startGame with explicit first to move returns an Ongoing game in its initial state`() {
        val expectedFirstToMove = Marker.CROSS
        val sut = startGame(expectedFirstToMove)

        assertEquals(expectedFirstToMove, sut.turn)
        sut.board.flatten().forEach { assertNull(it) }
    }

    @Test
    fun `startGame with no explicit first to move returns an Ongoing game in its initial state`() {
        val sut = startGame()

        assertEquals(Marker.firstToMove, sut.turn)
        sut.board.flatten().forEach { assertNull(it) }
    }

    @Test
    fun `makeMove on an Ongoing game succeeds and changes turn`() {
        // Arrange
        val sut = startGame()
        val firstTurn = sut.turn
        val at = Coordinate(row = 1, column = 2)

        // Act
        val gameAfterMove = sut.makeMove(at)

        // Assert
        xAssertIs<Game.Ongoing>(gameAfterMove)
        assertEquals(firstTurn, gameAfterMove.board[at])
    }

    @Test
    fun `makeMove on a Finished game throws`() {
        // Arrange
        val sut = startGame().forfeit()
        val at = Coordinate(row = 1, column = 2)

        // Act & Assert
        assertThrows(IllegalStateException::class.java) {
            sut.makeMove(at)
        }
    }

    @Test
    fun `makeMove on non empty position throws`() {
        // Arrange
        val at = Coordinate(row = 1, column = 2)
        val sut = startGame().makeMove(at)

        // Act & Assert
        assertThrows(IllegalArgumentException::class.java) {
            sut.makeMove(at)
        }
    }

    @Test
    fun `makeMove on the last available tile ties the game`() {
        // Arrange
        val lastAvailable = Coordinate(row = 0, column = 0)
        val lastTurn = Marker.CIRCLE
        val sut = Game.Ongoing(
            turn = lastTurn,
            board = Board(
                tiles = listOf(
                    listOf(null, Marker.CROSS, Marker.CIRCLE),
                    listOf(Marker.CROSS, Marker.CIRCLE, Marker.CROSS),
                    listOf(Marker.CROSS, Marker.CIRCLE, Marker.CROSS)
                )
            )
        )

        // Act
        val gameAfterMove = sut.makeMove(lastAvailable)

        // Assert
        xAssertIs<Game.Draw>(gameAfterMove)
        assertEquals(lastTurn, gameAfterMove.board[lastAvailable])
    }

    @Test
    fun `makeMove on a tile that wins the game attributes the win to the marker`() {
        // Arrange
        val winningMarker = Marker.CIRCLE
        val winningPosition = Coordinate(row = 2, column = 0)
        val sut = Game.Ongoing(
            turn = winningMarker,
            board = Board(
                tiles = listOf(
                    listOf(null, null, Marker.CIRCLE),
                    listOf(Marker.CROSS, Marker.CIRCLE, Marker.CROSS),
                    listOf(null, null, Marker.CROSS)
                )
            )
        )

        // Act
        val gameAfterMove = sut.makeMove(winningPosition)

        // Assert
        xAssertIs<Game.HasWinner>(gameAfterMove)
        assertEquals(winningMarker, gameAfterMove.winner)
    }

    @Test
    fun `forfeit on a Finished game throws`() {
        // Arrange
        val sut = startGame().forfeit()

        // Act & Assert
        assertThrows(IllegalStateException::class.java) {
            sut.forfeit()
        }
    }

    @Test
    fun `forfeit on an Ongoing game finishes it and attributes the win to the other marker`() {
        // Arrange
        val sut = startGame()
        val firstTurn = sut.turn

        // Act
        val gameAfterForfeit = sut.forfeit()

        // Assert
        xAssertIs<Game.HasWinner>(gameAfterForfeit)
        assertEquals(firstTurn.other, gameAfterForfeit.winner)
    }
}