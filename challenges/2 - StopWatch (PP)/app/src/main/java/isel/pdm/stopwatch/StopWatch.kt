@file:Suppress("MemberVisibilityCanBePrivate", "unused")
package isel.pdm.stopwatch

/**
 * Represents a stop watch. Instances of this class are immutable.
 *
 * @constructor Creates an instance of [StopWatch] with the given [startedAt] and [stoppedAt] values.
 * @property startedAt the time when the stop watch was started
 * @property stoppedAt the time when the stop watch was stopped, or null if it is still running
 * @property totalMilliseconds the total number of milliseconds elapsed since the stop watch was started
 * @property value the stop watch value in minutes, seconds and milliseconds
 * @property isStarted true if the stop watch is started, false otherwise
 * @property isStopped true if the stop watch is stopped, false otherwise
 */
data class StopWatch(val startedAt: Long, val stoppedAt: Long? = null) {

    init {
        require(startedAt >= 0) { "startedAt must be non-negative" }
        require(stoppedAt == null || stoppedAt >= startedAt) { "stoppedAt must be non-negative and greater than startedAt" }
    }

    val totalMilliseconds: Long
        get() = (stoppedAt ?: System.currentTimeMillis()) - startedAt

    val value: StopWatchValue
        get() = StopWatchValue(
            minutes = (totalMilliseconds / 1000 / 60).toInt(),
            seconds = (totalMilliseconds / 1000 % 60).toInt(),
            milliseconds = (totalMilliseconds % 1000).toInt()
        )

    val isStarted: Boolean
        get() = stoppedAt == null

    val isStopped: Boolean
        get() = !isStarted

    val isZero: Boolean
        get() = totalMilliseconds == 0L

    /**
     * Starts the stop watch.
     * @return a new instance of [StopWatch] with the [startedAt] property set to the current time
     * @throws IllegalStateException if the stop watch is already started
     */
    fun start(): StopWatch {
        check(isStopped) { "Stop Watch is already started" }
        return copy(startedAt = System.currentTimeMillis(), stoppedAt = null)
    }

    /**
     * Stops the stop watch.
     * @return a new instance of [StopWatch] with the [stoppedAt] property set to the current time
     * @throws IllegalStateException if the stop watch is already stopped
     */
    fun stop(): StopWatch {
        check(isStarted) { "Stop Watch is already stopped" }
        return copy(stoppedAt = System.currentTimeMillis())
    }

    /**
     * Resumes the stop watch.
     * @return a new instance of [StopWatch] with the same startedAt moment and no stoppedAt moment
     */
    fun resume(): StopWatch {
        return copy(startedAt = System.currentTimeMillis() - totalMilliseconds, stoppedAt = null)
    }
}

/**
 * Represents a stop watch value. Instances of this class are immutable.
 * @property minutes the number of minutes
 * @property seconds the number of seconds
 * @property milliseconds the number of milliseconds
 */
data class StopWatchValue(val minutes: Int, val seconds: Int, val milliseconds: Int) {
    init {
        require(minutes >= 0) { "minutes must be non-negative" }
        require(seconds in 0..59) { "seconds must be between 0 and 59" }
        require(milliseconds in 0..999) { "milliseconds must be between 0 and 999" }
    }
}
