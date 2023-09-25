package isel.pdm.capacitymonitor

import org.junit.Test

class OccupancyTests {

    @Test(expected = IllegalArgumentException::class)
    fun `occupancy cannot be negative`() {
        Occupancy(10, -1)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `capacity cannot be negative`() {
        Occupancy(-1, 0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `occupancy cannot be greater than capacity`() {
        Occupancy(10, 11)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `capacity cannot be zero`() {
        Occupancy(0, 0)
    }

    @Test
    fun `is full returns true when capacity has been reached`() {
        val occupancy = Occupancy(10, 10)
        assert(occupancy.isFull())
    }

    @Test
    fun `is full returns false when capacity has not been reached`() {
        val occupancy = Occupancy(10, 9)
        assert(!occupancy.isFull())
    }

    @Test
    fun `is empty returns true when occupancy is zero`() {
        val occupancy = Occupancy(10, 0)
        assert(occupancy.isEmpty())
    }

    @Test
    fun `is empty returns false when occupancy is not zero`() {
        val occupancy = Occupancy(10, 1)
        assert(!occupancy.isEmpty())
    }

    @Test
    fun `incrementing occupancy increases it by one`() {
        val occupancy = Occupancy(10, 0)
        val incremented = occupancy.inc()
        assert(incremented.current == occupancy.current + 1)
    }

    @Test(expected = IllegalStateException::class)
    fun `incrementing occupancy when room is full throws`() {
        val occupancy = Occupancy(10, 10)
        occupancy.inc()
    }

    @Test
    fun `decrementing occupancy decreases it by one`() {
        val sut = Occupancy(10, 1)
        val result = sut.dec()
        assert(result.current == sut.current - 1)
    }

    @Test(expected = IllegalStateException::class)
    fun `decrementing occupancy when room is empty throws`() {
        val occupancy = Occupancy(10, 0)
        occupancy.dec()
    }
}