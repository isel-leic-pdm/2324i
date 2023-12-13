package isel.pdm.demos.tictactoe.domain.game.play

import org.junit.Assert
import org.junit.Test

class CoordinateTests {
    @Test
    fun `isValidRow returns true when in the board bounds indexes`() {
        val aValidRow = 2
        Assert.assertTrue(isValidRow(aValidRow))
    }

    @Test
    fun `isValidRow returns false for negative index`() {
        val anInvalidRow = -4
        Assert.assertFalse(isValidRow(anInvalidRow))
    }

    @Test
    fun `isValidColumn returns true when in the board bounds indexes`() {
        val aValidColumn = 0
        Assert.assertTrue(isValidColumn(aValidColumn))
    }

    @Test
    fun `isValidColumn returns false for negative index`() {
        val anInvalidColumn = -1
        Assert.assertFalse(isValidRow(anInvalidColumn))
    }

    @Test(expected = IllegalArgumentException::class)
    fun `create Coordinate with invalid row throws`() {
        val anInvalidRow = -2
        val aValidColumn = 1
        Coordinate(row = anInvalidRow, column = aValidColumn)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `create Coordinate with invalid column throws`() {
        val aValidRow = 2
        val anInvalidColumn = -3
        Coordinate(row = aValidRow, column = anInvalidColumn)
    }

    @Test
    fun `create Coordinate with valid coordinates succeeds`() {
        val aValidRow = 2
        val aValidColumn = 1
        Coordinate(row = aValidRow, column = aValidColumn)
    }

    @Test
    fun `fromIndex with a valid index returns a Coordinate with the correct row and column`() {
        val aValidIndex = 5
        val expectedRow = 1
        val expectedColumn = 2
        val sut = Coordinate.fromIndex(aValidIndex)
        Assert.assertEquals(expectedRow, sut.row)
        Assert.assertEquals(expectedColumn, sut.column)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `fromIndex with an invalid index throws`() {
        val anInvalidIndex = -1
        Coordinate.fromIndex(anInvalidIndex)
    }

    @Test
    fun `toIndex with a valid Coordinate returns the correct index`() {
        val aValidCoordinate = Coordinate(row = 1, column = 2)
        val expectedIndex = 5
        val sut = aValidCoordinate.toIndex()
        Assert.assertEquals(expectedIndex, sut)
    }
}