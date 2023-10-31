package isel.pdm.jokes.presentation

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import isel.pdm.jokes.domain.Idle
import isel.pdm.jokes.domain.Joke
import isel.pdm.jokes.domain.JokesService
import isel.pdm.jokes.domain.Loaded
import isel.pdm.jokes.domain.Loading
import isel.pdm.jokes.domain.Term
import isel.pdm.jokes.domain.getOrNull
import isel.pdm.jokes.domain.getOrThrow
import isel.pdm.jokes.search.SearchScreenViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.net.URL

@OptIn(ExperimentalCoroutinesApi::class)
class SearchScreenViewModelTests {

    private val jokes = listOf(
        Joke("This is a test joke", URL("https://www.test.pt")),
        Joke("Yet another test joke", URL("https://www.test.pt"))
    )

    @get:Rule
    val rule = MockMainDispatcherRule(UnconfinedTestDispatcher())

    @Test
    fun `initially the search is in idle state`() = runTest {
        // Arrange
        val sut = SearchScreenViewModel(mockk())
        // Act
        // Assert
        assertTrue(sut.jokes is Idle)
    }

    @Test
    fun `calling search places the search in loading state`() = runTest {
        // Arrange
        val service = mockk<JokesService> {
            coEvery { searchJokes(any()) } coAnswers { delay(1000); jokes }
        }
        val sut = SearchScreenViewModel(service)
        // Act
        sut.search(term = Term("test"))
        // Assert
        assertTrue(sut.jokes is Loading)
    }

    @Test
    fun `calling search calls the service`() = runTest {
        // Arrange
        val service = mockk<JokesService> {
            coEvery { searchJokes(any()) } coAnswers { delay(1000); jokes }
        }
        val sut = SearchScreenViewModel(service)
        // Act
        sut.search(term = Term("test"))
        rule.advanceUntilIdle()
        // Assert
        coVerify(exactly = 1) { service.searchJokes(term = Term("test")) }
    }

    @Test
    fun `successfully searching for jokes publishes them`() = runTest {
        // Arrange
        val service = mockk<JokesService> {
            coEvery { searchJokes(any()) } coAnswers { delay(1000); jokes }
        }
        val sut = SearchScreenViewModel(service)
        // Act
        sut.search(term = Term("test"))
        rule.advanceUntilIdle()
        // Assert
        assertTrue(sut.jokes is Loaded)
        assertSame(jokes, sut.jokes.getOrNull())
    }

    @Test
    fun `failing to search for jokes publishes the error`() = runTest {
        // Arrange
        val error = Exception("Test")
        val service = mockk<JokesService> {
            coEvery { searchJokes(any()) } coAnswers { delay(1000); throw error }
        }
        val sut = SearchScreenViewModel(service)
        // Act
        sut.search(term = Term("test"))
        rule.advanceUntilIdle()
        // Assert
        assertTrue(sut.jokes is Loaded)
        Assert.assertThrows(Exception::class.java) { sut.jokes.getOrThrow() }
    }
}
