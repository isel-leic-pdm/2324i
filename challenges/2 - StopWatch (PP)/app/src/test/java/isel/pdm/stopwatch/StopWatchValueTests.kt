package isel.pdm.stopwatch

import org.junit.Test

class StopWatchValueTests {

    @Test(expected = IllegalArgumentException::class)
    fun `instantiate with negative milliseconds value throws`() {
        StopWatchValue(minutes = 0, seconds = 0, milliseconds = -1)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `instantiate with negative seconds value throws`() {
        StopWatchValue(minutes = 0, seconds = -1, milliseconds = 0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `instantiate with negative minutes value throws`() {
        StopWatchValue(minutes = -1, seconds = 0, milliseconds = 0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `instantiate with seconds value greater than 59 throws`() {
        StopWatchValue(minutes = 0, seconds = 60, milliseconds = 0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `instantiate with milliseconds value greater than 999 throws`() {
        StopWatchValue(minutes = 0, seconds = 0, milliseconds = 1000)
    }

    @Test
    fun `instantiate with valid values does not throw`() {
        StopWatchValue(minutes = 2, seconds = 51, milliseconds = 236)
    }
}