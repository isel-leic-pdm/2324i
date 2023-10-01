package isel.pdm.stopwatch

import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class StopWatchTests {

    @Test(expected = IllegalStateException::class)
    fun `start on a started stopWatch throws`() {
        // Arrange
        val sut = StopWatch(startedAt = 0)
        // Act
        sut.start()
    }

    @Test(expected = IllegalStateException::class)
    fun `stop on a stopped stopWatch throws`() {
        // Arrange
        val sut = StopWatch(startedAt = 0, stoppedAt = 1)
        // Act
        sut.stop()
    }

    @Test
    fun `start on a stopped stopWatch returns a started one`() {
        // Arrange
        val sut = StopWatch(startedAt = 0, stoppedAt = 1)
        // Act
        val result = sut.start()
        // Assert
        assertTrue(result.startedAt > 0)
        assertNull(result.stoppedAt)
    }

    @Test
    fun `stop on a started stopWatch returns a stopped one`() {
        // Arrange
        val sut = StopWatch(startedAt = 0)
        // Act
        val result = sut.stop()
        // Assert
        assertNotNull(result.stoppedAt)
        assertTrue(result.stoppedAt!! > 0)
    }

    @Test
    fun `isStarted on a started stopWatch returns true`() {
        // Arrange
        val sut = StopWatch(startedAt = 0)
        // Act
        // Assert
        assertTrue(sut.isStarted)
    }

    @Test
    fun `isStarted on a stopped stopWatch returns false`() {
        // Arrange
        val sut = StopWatch(startedAt = 0, stoppedAt = 1)
        // Act
        // Assert
        assertFalse(sut.isStarted)
    }

    @Test
    fun `isStopped on a started stopWatch returns false`() {
        // Arrange
        val sut = StopWatch(startedAt = 0)
        // Act
        // Assert
        assertFalse(sut.isStopped)
    }

    @Test
    fun `isStopped on a stopped stopWatch returns true`() {
        // Arrange
        val sut = StopWatch(startedAt = 0, stoppedAt = 1)
        // Act
        // Assert
        assertTrue(sut.isStopped)
    }

    @Test
    fun `value on a stopWatch returns the correct value`() {
        // Arrange
        val sut = StopWatch(startedAt = 0, stoppedAt = 154432)
        // Act
        val result = sut.value
        // Assert
        assertTrue(result.minutes == 2)
        assertTrue(result.seconds == 34)
        assertTrue(result.milliseconds == 432)
    }
}



