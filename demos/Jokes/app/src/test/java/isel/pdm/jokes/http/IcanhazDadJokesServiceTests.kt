package isel.pdm.jokes.http

import isel.pdm.jokes.domain.FetchJokeException
import isel.pdm.jokes.domain.Term
import isel.pdm.jokes.http.providers.IcanhazDadJoke
import isel.pdm.jokes.http.providers.IcanhazDadJoke.JokeDto
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.net.URL

@OptIn(ExperimentalCoroutinesApi::class)
class IcanhazDadJokesServiceTests {

    @get:Rule
    val rule = MockWebServerRule()

    @Test
    fun `fetchJoke returns joke produced by the API`() {
        // Arrange
        val jokeDto = JokeDto(id = "1234", joke = "This is a joke", status = 200)
        val expected = jokeDto.toJoke()
        rule.webServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json")
                .setBody(rule.gson.toJson(jokeDto))
        )

        val sut = IcanhazDadJoke(
            client = rule.httpClient,
            gson = rule.gson,
            randomRequestUrl = rule.webServer.url("/").toUrl(),
            searchRequestUrl = URL("http://irrelevant.example")
        )

        // Act
        val actual = runBlocking { sut.fetchJoke() }

        // Assert
        assertEquals(expected, actual)
    }

    @Test(expected = FetchJokeException::class)
    fun `fetchJoke throws FetchJokeException on API access timeout`() {
        // Arrange
        val sut = IcanhazDadJoke(
            client = rule.httpClient,
            gson = rule.gson,
            randomRequestUrl = rule.webServer.url("/").toUrl(),
            searchRequestUrl = URL("http://irrelevant.example")
        )

        // Act
        runBlocking { sut.fetchJoke() }
    }

    @Test(expected = FetchJokeException::class)
    fun `fetchJoke throws FetchJokeException when API returns 500`() {
        // Arrange
        rule.webServer.enqueue(
            MockResponse().setResponseCode(500)
        )

        val sut = IcanhazDadJoke(
            client = rule.httpClient,
            gson = rule.gson,
            randomRequestUrl = rule.webServer.url("/").toUrl(),
            searchRequestUrl = URL("http://irrelevant.example")
        )

        // Act
        runBlocking { sut.fetchJoke() }
    }

    @Test
    fun `fetchJoke throws CancellationException when coroutine is cancelled`() = runTest {
        // Arrange
        val sut = IcanhazDadJoke(
            client = rule.httpClient,
            gson = rule.gson,
            randomRequestUrl = rule.webServer.url("/").toUrl(),
            searchRequestUrl = URL("http://irrelevant.example")
        )
        var cancellationThrown = false

        // Act
        val job = launch(UnconfinedTestDispatcher()) {
            try {
                sut.fetchJoke()
            } catch (e: CancellationException) {
                cancellationThrown = true
                throw e
            }
        }
        job.cancel()
        job.join()

        // Assert
        assertEquals(true, cancellationThrown)
    }

    @Test
    fun `searchJokes returns jokes produced by the API`() {
        // Arrange
        val jokeDto =
            IcanhazDadJoke.SearchResultItemDto(id = "1234", joke = "This is a joke")
        val searchResultDto = IcanhazDadJoke.SearchResultDto(
            currentPage = 1,
            limit = 1,
            nextPage = 2,
            previousPage = 0,
            results = listOf(jokeDto),
            searchTerm = "term",
            status = 200,
            totalJokes = 1,
            totalPages = 1
        )
        val expected = searchResultDto.toJokeList()
        rule.webServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json")
                .setBody(rule.gson.toJson(searchResultDto))
        )

        val sut = IcanhazDadJoke(
            client = rule.httpClient,
            gson = rule.gson,
            randomRequestUrl = URL("http://irrelevant.example"),
            searchRequestUrl = rule.webServer.url("/").toUrl()
        )

        // Act
        val actual = runBlocking { sut.searchJokes(Term("term")) }

        // Assert
        assertEquals(expected.size, actual.size)
        assertTrue(expected.containsAll(actual))
    }

    @Test(expected = FetchJokeException::class)
    fun `searchJokes throws FetchJokeException on API access timeout`() {
        // Arrange
        val sut = IcanhazDadJoke(
            client = rule.httpClient,
            gson = rule.gson,
            randomRequestUrl = URL("http://irrelevant.example"),
            searchRequestUrl = rule.webServer.url("/").toUrl()
        )

        // Act
        runBlocking { sut.searchJokes(Term("term")) }
    }

    @Test(expected = FetchJokeException::class)
    fun `searchJokes throws FetchJokeException when API returns 500`() {
        // Arrange
        rule.webServer.enqueue(
            MockResponse().setResponseCode(500)
        )

        val sut = IcanhazDadJoke(
            client = rule.httpClient,
            gson = rule.gson,
            randomRequestUrl = URL("http://irrelevant.example"),
            searchRequestUrl = rule.webServer.url("/").toUrl()
        )

        // Act
        runBlocking { sut.searchJokes(Term("term")) }
    }

    @Test
    fun `searchJokes throws CancellationException when coroutine is cancelled`() = runTest {
        // Arrange
        val sut = IcanhazDadJoke(
            client = rule.httpClient,
            gson = rule.gson,
            randomRequestUrl = URL("http://irrelevant.example"),
            searchRequestUrl = rule.webServer.url("/").toUrl()
        )
        var cancellationThrown = false

        // Act
        val job = launch(UnconfinedTestDispatcher()) {
            try {
                sut.searchJokes(Term("term"))
            } catch (e: CancellationException) {
                cancellationThrown = true
                throw e
            }
        }
        job.cancel()
        job.join()

        // Assert
        assertEquals(true, cancellationThrown)
    }
}