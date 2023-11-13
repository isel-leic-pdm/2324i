package isel.pdm.demos.tictactoe

import isel.pdm.demos.tictactoe.domain.UserInfo
import isel.pdm.demos.tictactoe.domain.validateUserInfoParts
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class UserInfoTests {

    @Test
    fun `creating an instance with non blank nick and moto succeeds`() {
        UserInfo("nickname", "moto")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `creating an instance with blank nickname throws IllegalArgumentException`() {
        UserInfo("  ", "moto")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `creating an instance with blank moto throws IllegalArgumentException`() {
        UserInfo("nickname", "  ")
    }

    @Test
    fun `creating an instance with non blank nick and no moto succeeds`() {
        UserInfo("nickname")
    }

    @Test
    fun `validateUserInfoParts with non blank nick and moto returns true`() {
        // Arrange
        // Act
        val result = validateUserInfoParts("nickname", "moto")
        // Assert
        assertTrue(result)
    }

    @Test
    fun `validateUserInfoParts with non blank nick and no moto returns true`() {
        // Arrange
        // Act
        val result = validateUserInfoParts("nickname", null)
        // Assert
        assertTrue(result)
    }

    @Test
    fun `validateUserInfoParts with blank nick and non blank moto returns false`() {
        // Arrange
        // Act
        val result = validateUserInfoParts("  ", "moto")
        // Assert
        assertFalse(result)
    }

    @Test
    fun `validateUserInfoParts with blank nick and no moto returns false`() {
        // Arrange
        // Act
        val result = validateUserInfoParts("  ", null)
        // Assert
        assertFalse(result)
    }
}