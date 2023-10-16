package isel.pdm.jokes.presentation

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import isel.pdm.jokes.Joke
import isel.pdm.jokes.JokesService
import isel.pdm.jokes.daily.JokeScreenViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.net.URL

@OptIn(ExperimentalCoroutinesApi::class)
class JokeScreenViewModelTests {

    private val sut = JokeScreenViewModel()

    @get:Rule
    val rule = MockMainDispatcherRule(UnconfinedTestDispatcher())

    @Test
    fun `initially the joke is null`() {
        // Arrange
        // Act
        // Assert
        assertNull(sut.joke)
    }

    @Test
    fun `fetch joke calls service`() {
//        // Arrange
//        var called = false
//        // Act
//        sut.fetchJoke(object : JokesService {
//            override suspend fun fetchJoke(): Joke {
//                called = true
//                return Joke("Test", URL("https://www.test.pt"))
//            }
//        })
//        // Assert
//        assertTrue(called)
        // Arrange
        val service = mockk<JokesService> {
            coEvery { fetchJoke() } coAnswers {
                Joke("Test", URL("https://www.test.pt"))
            }
        }
        // Act
        sut.fetchJoke(service)
        // Assert
        coVerify(exactly = 1) { service.fetchJoke() }
    }
}