package isel.pdm.capacitymonitor

/**
 * Represents the occupancy of a given room. Instances are immutable.
 *
 * @property capacity The room's capacity.
 * @property current The room's current occupancy.
 */
data class Occupancy(val capacity: Int, val current: Int = 0) {

    /**
     * Initializes a new instance while ensuring that it is valid according to the
     * domain rules.
     */
    init {
        require(capacity > 0)
        require(current >= 0)
        require(current <= capacity)
    }

    /**
     * Checks if the room is full.
     */
    fun isFull(): Boolean = current == capacity

    /**
     * Checks if the room is empty.
     */
    fun isEmpty(): Boolean = current == 0

    /**
     * Increments the room's occupancy, returning a new instance.
     * @return The incremented occupancy.
     * @throws IllegalStateException if the room is already full.
     */
    operator fun inc(): Occupancy = let {
        check(isNotFull()) { "Room capacity already reached" }
        copy(current = current + 1)
    }

    /**
     * Decrements the room's occupancy, returning a new instance.
     * @return The decremented occupancy.
     * @throws IllegalStateException if the room is already empty.
     */
    operator fun dec(): Occupancy = let {
        check(isNotEmpty()) { "Room is already empty" }
        copy(current = current - 1)
    }
}

// Extension functions, for the sake of expressiveness

/**
 * Checks if the room is not full.
 */
fun Occupancy.isNotFull(): Boolean = !isFull()

/**
 * Checks if the room is not empty.
 */
fun Occupancy.isNotEmpty(): Boolean = !isEmpty()

