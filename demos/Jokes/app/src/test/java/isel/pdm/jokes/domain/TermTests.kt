package isel.pdm.jokes.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class TermTests {

    @Test
    fun `create term with valid arguments succeeds`() {
        Term("test")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `create term with blank text fails`() {
        Term("")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `create term with text containing only blanks fails`() {
        Term("  \t  \t  ")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `create term with less than 3 characters fails`() {
        Term("te")
    }

    @Test
    fun `toTermOrNull on valid term returns term`() {
        val term = "test"
        val result = term.toTermOrNull()
        assertNotNull(result)
        assertEquals(term, result!!.value)
    }
}