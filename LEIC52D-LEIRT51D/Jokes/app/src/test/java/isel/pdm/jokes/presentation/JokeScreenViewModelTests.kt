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
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.net.URL

@OptIn(ExperimentalCoroutinesApi::class)
class JokeScreenViewModelTests {

    @get:Rule
    val mainDispatcherRule = MockMainDispatcherRule(UnconfinedTestDispatcher())

    @Test
    fun `initially the joke is null`() {
        // Arrange
        val sut = JokeScreenViewModel()
        // Act
        // Assert
        assertTrue(sut.joke == null)
    }

    @Test
    fun `fetchJoke calls service`() {
        // Arrange
        val sut = JokeScreenViewModel()
        val joke = Joke(text = "The joke", source = URL("https://www.tests.com"))
        val service = mockk<JokesService> {
            coEvery { fetchJoke() } coAnswers { delay(1000); joke }
        }
        // Act
        sut.fetchJoke(service)
        // Assert
        coVerify(exactly = 1) { service.fetchJoke() }
    }
}