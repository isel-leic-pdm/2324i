package isel.pdm.jokes.presentation

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import isel.pdm.jokes.Joke
import isel.pdm.jokes.JokesService
import isel.pdm.jokes.daily.JokeScreenViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test
import java.net.URL

@OptIn(ExperimentalCoroutinesApi::class)
class JokeScreenViewModelTests {

    @get:Rule
    val rule = MockMainDispatcherRule(UnconfinedTestDispatcher())

    private val sut = JokeScreenViewModel()

    @Test
    fun `initially the joke is null`() {
        // Arrange
        // Act
        // Assert
        assertNull(sut.joke)
    }

    @Test
    fun `fetchJoke triggers call to service`() {
        // Arrange
        val serviceMock = mockk<JokesService> {
            coEvery { fetchJoke() } coAnswers {
                delay(1000)
                Joke("Test", URL("https://www.test.pt"))
            }
        }
        // Act
        sut.fetchJoke(serviceMock)
        // Assert
        coVerify(exactly = 1) { serviceMock.fetchJoke() }
    }

    @Test
    fun `fetchJoke publishes joke`() {
        // Arrange
        val expected = Joke("Test", URL("https://www.test.pt"))
        val serviceMock = mockk<JokesService> {
            coEvery { fetchJoke() } coAnswers {
                delay(1000)
                expected
            }
        }
        // Act
        sut.fetchJoke(serviceMock)
        rule.advanceUntilIdle()

        // Assert
        val actual = sut.joke
        assertNotNull(actual)
        assertEquals(expected, actual)
    }
}