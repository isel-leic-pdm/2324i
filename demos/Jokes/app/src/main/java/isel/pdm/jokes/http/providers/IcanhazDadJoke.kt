package isel.pdm.jokes.http.providers

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import isel.pdm.jokes.domain.FetchJokeException
import isel.pdm.jokes.domain.Joke
import isel.pdm.jokes.domain.JokesService
import isel.pdm.jokes.domain.Term
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.net.URL
import kotlin.coroutines.resumeWithException

private const val ICANHAZ_API_URL = "https://icanhazdadjoke.com/"
private const val ICANHAZ_SEARCH_URL = "${ICANHAZ_API_URL}search"

/**
 * Jokes provider that fetches jokes from the IcanhazDadJoke API.
 * @see <a href="https://icanhazdadjoke.com/api">https://icanhazdadjoke.com/api</a>
 *
 * @param client The HTTP client used to perform the requests.
 * @param gson The JSON serializer/deserializer used to convert JSON into DTOs.
 * @param randomRequestUrl The URL used to fetch a random joke. Defaults to the IcanhazDadJoke API
 * but can be changed for testing purposes.
 * @param searchRequestUrl The URL used to search for jokes. Defaults to the IcanhazDadJoke API
 * but can be changed for testing purposes.
 */
class IcanhazDadJoke(
    private val client: OkHttpClient,
    private val gson: Gson,
    randomRequestUrl: URL = URL(ICANHAZ_API_URL),
    searchRequestUrl: URL = URL(ICANHAZ_SEARCH_URL)
) : JokesService {

    private val randomJokeRequest by lazy {
        Request.Builder()
            .url(randomRequestUrl)
            .addHeader("accept", "application/json")
            .build()
    }

    private val searchRequest by lazy {
        Request.Builder()
            .url(searchRequestUrl)
            .addHeader("accept", "application/json")
            .build()
    }

    /**
     * Fetches a joke from the IcanhazDadJoke API.
     */
    override suspend fun fetchJoke(): Joke = suspendCancellableCoroutine {
        val call = client.newCall(randomJokeRequest)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                it.resumeWithException(FetchJokeException("Failed to fetch joke", e))
            }
            override fun onResponse(call: Call, response: Response) {
                val body = response.body
                if (!response.isSuccessful || body == null)
                    it.resumeWithException(FetchJokeException("Failed to fetch joke: ${response.code}"))
                else
                    it.resumeWith(Result.success(gson.fromJson(body.string(), JokeDto::class.java).toJoke()))
            }
        })

        it.invokeOnCancellation { call.cancel() }
    }

    /**
     * Searches for jokes containing the given term.
     */
    override suspend fun searchJokes(term: Term): List<Joke> = suspendCancellableCoroutine {

        val urlWithParams = searchRequest
            .url
            .newBuilder()
            .addQueryParameter("term", term.value)
            .build()
        val requestWithParams = searchRequest.newBuilder().url(urlWithParams).build()

        val call = client.newCall(requestWithParams)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                it.resumeWithException(FetchJokeException("Failed to search jokes", e))
            }
            override fun onResponse(call: Call, response: Response) {
                val body = response.body
                if (!response.isSuccessful || body == null)
                    it.resumeWithException(FetchJokeException("Failed to search jokes: ${response.code}"))
                else
                    it.resumeWith(Result.success(gson.fromJson(body.string(), SearchResultDto::class.java).toJokeList()))
            }
        })

        it.invokeOnCancellation { call.cancel() }
    }

    /**
     * The DTO used to represent a joke obtained from the remote service.
     */
    data class JokeDto(val id: String, val joke: String, val status: Int) {
        fun toJoke() = Joke(text = joke, source = URL(ICANHAZ_API_URL))
    }

    /**
     * The DTO used to represent a search result obtained from the remote service.
     */
    data class SearchResultDto(
        @SerializedName("current_page")
        val currentPage: Int,
        val limit: Int,
        @SerializedName("next_page")
        val nextPage: Int,
        @SerializedName("previous_page")
        val previousPage: Int,
        val results: List<SearchResultItemDto>,
        @SerializedName("search_term")
        val searchTerm: String,
        val status: Int,
        @SerializedName("total_jokes")
        val totalJokes: Int,
        @SerializedName("total_pages")
        val totalPages: Int
    ) {
        fun toJokeList(): List<Joke> = results.map { item -> item.toJoke() }
    }

    data class SearchResultItemDto(val id: String, val joke: String) {
        fun toJoke() = Joke(text = joke, source = URL(ICANHAZ_API_URL))
    }
}
