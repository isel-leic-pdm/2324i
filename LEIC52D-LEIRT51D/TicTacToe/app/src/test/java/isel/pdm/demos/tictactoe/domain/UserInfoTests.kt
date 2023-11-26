package isel.pdm.demos.tictactoe.domain

import isel.pdm.demos.tictactoe.domain.user.UserInfo
import isel.pdm.demos.tictactoe.domain.user.toUserInfoOrNull
import isel.pdm.demos.tictactoe.domain.user.validateUserInfoParts
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class UserInfoTests {

    @Test
    fun `creating an user info with a non-blank nick and a non-blank moto succeeds`() {
        UserInfo(nick = "test user", motto = "test moto")
    }

    @Test
    fun `creating an user info with a non-blank nick and no moto succeeds`() {
        UserInfo(nick = "test user")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `creating an user info with a blank nick throws`() {
        UserInfo(nick = "  ")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `creating an user info with a blank moto throws`() {
        UserInfo(nick = "test user", motto = "  ")
    }

    @Test
    fun `validating an user info with a non-blank nick and a non-blank moto succeeds`() {
        // Arrange
        // Act
        val result = validateUserInfoParts(nick = "test user", motto = "test moto")
        // Assert
        assertTrue(result)
    }

    @Test
    fun `validating an user info with a non-blank nick and no moto succeeds`() {
        // Arrange
        // Act
        val result = validateUserInfoParts(nick = "test user", motto = null)
        // Assert
        assertTrue(result)
    }

    @Test
    fun `toUserInfoOrNull with a non-blank nick and a non-blank moto succeeds`() {
        // Arrange
        // Act
        val result = toUserInfoOrNull(nick = "test user", motto = "test moto")
        // Assert
        assertNotNull(result)
    }

    @Test
    fun `toUserInfoOrNull with a non-blank nick and no moto succeeds`() {
        // Arrange
        // Act
        val result = toUserInfoOrNull(nick = "test user", motto = null)
        // Assert
        assertNotNull(result)
    }

    @Test
    fun `toUserInfoOrNull with a blank nick and a non-blank moto returns null`() {
        // Arrange
        // Act
        val result = toUserInfoOrNull(nick = "  ", motto = "test moto")
        // Assert
        assertNull(result)
    }
}