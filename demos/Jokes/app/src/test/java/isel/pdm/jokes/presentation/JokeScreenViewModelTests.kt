package isel.pdm.jokes.presentation

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import isel.pdm.jokes.domain.Idle
import isel.pdm.jokes.domain.Joke
import isel.pdm.jokes.domain.JokesService
import isel.pdm.jokes.domain.Loaded
import isel.pdm.jokes.domain.Loading
import isel.pdm.jokes.daily.JokeScreenViewModel
import isel.pdm.jokes.domain.getOrNull
import isel.pdm.jokes.domain.getOrThrow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.net.URL

@OptIn(ExperimentalCoroutinesApi::class)
class JokeScreenViewModelTests {

    val joke = Joke("Test", URL("https://www.test.pt"))

    @get:Rule
    val rule = MockMainDispatcherRule(UnconfinedTestDispatcher())

    @Test
    fun `initially the joke is in idle state`() = runTest {
        val sut = JokeScreenViewModel(mockk())
        assertTrue(sut.joke is Idle)
    }

    @Test
    fun `fetching a joke places the joke in loading state`() = runTest {
        // Arrange
        val service = mockk<JokesService> {
            coEvery { fetchJoke() } coAnswers { delay(1000); joke }
        }
        val sut = JokeScreenViewModel(service)
        // Act
        sut.fetchJoke()
        // Assert
        assertTrue(sut.joke is Loading)
    }

    @Test
    fun `fetching a joke calls the service`() = runTest {
        // Arrange
        val service = mockk<JokesService> {
            coEvery { fetchJoke() } coAnswers { delay(1000); joke }
        }
        val sut = JokeScreenViewModel(service)
        // Act
        sut.fetchJoke()
        rule.advanceUntilIdle()
        // Assert
        coVerify(exactly = 1) { service.fetchJoke() }
    }

    @Test
    fun `successfully fetching a joke publishes it`() = runTest {
        // Arrange
        val service = mockk<JokesService> {
            coEvery { fetchJoke() } coAnswers { delay(1000); joke }
        }
        val sut = JokeScreenViewModel(service)
        // Act
        sut.fetchJoke()
        rule.advanceUntilIdle()
        // Assert
        assertTrue(sut.joke is Loaded)
        assertEquals(joke, sut.joke.getOrNull())
    }

    @Test
    fun `failing to fetch a joke publishes the error`() = runTest {
        // Arrange
        val error = Exception("Test")
        val service = mockk<JokesService> {
            coEvery { fetchJoke() } coAnswers { delay(1000); throw error }
        }
        val sut = JokeScreenViewModel(service)
        // Act
        sut.fetchJoke()
        rule.advanceUntilIdle()
        // Assert
        assertTrue(sut.joke is Loaded)
        assertThrows(Exception::class.java) { sut.joke.getOrThrow() }
    }
}