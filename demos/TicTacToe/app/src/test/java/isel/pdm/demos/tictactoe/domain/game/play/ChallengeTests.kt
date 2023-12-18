package isel.pdm.demos.tictactoe.domain.game.play

import isel.pdm.demos.tictactoe.domain.game.lobby.Challenge
import isel.pdm.demos.tictactoe.domain.game.lobby.PlayerInfo
import isel.pdm.demos.tictactoe.domain.game.lobby.firstToMove
import isel.pdm.demos.tictactoe.domain.game.lobby.getMarkerFor
import isel.pdm.demos.tictactoe.domain.user.UserInfo
import org.junit.Assert.assertEquals
import org.junit.Test

class ChallengeTests {

    private val testChallenger = PlayerInfo(UserInfo("challenger"))
    private val testChallenged = PlayerInfo(UserInfo("challenged"))

    @Test
    fun firstToMove_returns_challenger() {
        // Arrange
        val sut = Challenge(challenger = testChallenger, challenged = testChallenged)

        // Act
        val result = sut.firstToMove

        // Assert
        assertEquals(sut.challenger, result)
    }

    @Test
    fun getMarker_returns_firstToMove_marker_for_challenger() {
        // Arrange
        val sut = Challenge(challenger = testChallenger, challenged = testChallenged)

        // Act
        val result = sut.getMarkerFor(testChallenger)

        // Assert
        assertEquals(Marker.firstToMove, result)
    }

    @Test
    fun getMarker_returns_secondToMove_marker_for_challenged() {
        // Arrange
        val sut = Challenge(challenger = testChallenger, challenged = testChallenged)

        // Act
        val result = sut.getMarkerFor(testChallenged)

        // Assert
        assertEquals(Marker.firstToMove.other, result)
    }
}