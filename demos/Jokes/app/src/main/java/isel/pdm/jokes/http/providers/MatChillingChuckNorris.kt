package isel.pdm.jokes.http.providers

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import isel.pdm.jokes.BuildConfig
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

private const val MAT_CHILLING_API_URL = "https://matchilling-chuck-norris-jokes-v1.p.rapidapi.com/jokes/random"
private const val MAT_CHILLING_SEARCH_URL = "https://matchilling-chuck-norris-jokes-v1.p.rapidapi.com/jokes/search"

/**
 * Jokes provider that fetches jokes from the Rapid API 's Mat Chilling Chuck Norris API.
 * @see <a href="https://rapidapi.com/matchilling/api/chuck-norris/">https://rapidapi.com/matchilling/api/chuck-norris/</a>
 *
 * @param client The HTTP client used to perform the requests
 * @param gson The JSON serializer/deserializer used to convert JSON into DTOs
 * @param randomRequestUrl The URL used to fetch a random joke. Defaults to the Mat Chilling Chuck Norris API
 * but can be changed for testing purposes.
 * @param searchRequestUrl The URL used to search for jokes. Defaults to the Mat Chilling Chuck Norris API
 * but can be changed for testing purposes.
 */
class MatChillingChuckNorris(
    private val client: OkHttpClient,
    private val gson: Gson,
    randomRequestUrl: URL = URL(MAT_CHILLING_API_URL),
    searchRequestUrl: URL = URL(MAT_CHILLING_SEARCH_URL)
) : JokesService {

    private val randomJokeRequest by lazy {
        Request.Builder()
            .url(randomRequestUrl)
            .addHeader("accept", "application/json")
            .addHeader("x-rapidapi-key", BuildConfig.RAPID_API_KEY)
            .addHeader("x-rapidapi-host", "matchilling-chuck-norris-jokes-v1.p.rapidapi.com")
            .build()
    }

    private val searchRequest by lazy {
        Request.Builder()
            .url(searchRequestUrl)
            .addHeader("accept", "application/json")
            .addHeader("x-rapidapi-key", BuildConfig.RAPID_API_KEY)
            .addHeader("x-rapidapi-host", "matchilling-chuck-norris-jokes-v1.p.rapidapi.com")
            .build()
    }

    /**
     * Fetches a joke from the Mat Chilling Chuck Norris API.
     */
    override suspend fun fetchJoke(): Joke = suspendCancellableCoroutine { continuation ->
        val call = client.newCall(randomJokeRequest)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                continuation.resumeWithException(FetchJokeException("Could not fetch joke", e))
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body
                if (!response.isSuccessful || body == null)
                    continuation.resumeWithException(
                        FetchJokeException("Could not fetch joke. Remote service returned ${response.code}")
                    )
                else
                    continuation.resumeWith(Result.success(gson.fromJson(body.string(), JokeDto::class.java).toJoke()))
            }
        })

        continuation.invokeOnCancellation { call.cancel() }
    }

    /**
     * Searches for jokes containing the given term.
     */
    override suspend fun searchJokes(term: Term): List<Joke> = suspendCancellableCoroutine { continuation ->
        val urlWithParams = searchRequest
            .url
            .newBuilder()
            .addQueryParameter("query", term.value)
            .build()
        val requestWithParams = searchRequest.newBuilder().url(urlWithParams).build()

        val call = client.newCall(requestWithParams)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                continuation.resumeWithException(FetchJokeException("Could not search jokes", e))
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body
                if (!response.isSuccessful || body == null)
                    continuation.resumeWithException(FetchJokeException("Could not search jokes. Remote service returned ${response.code}"))
                else
                    continuation.resumeWith(Result.success(gson.fromJson(body.string(), SearchResultDto::class.java).toJokeList()))
            }
        })

        continuation.invokeOnCancellation { call.cancel() }
    }

    /**
     * The DTO used to represent a joke obtained from the remote service.
     */
    private data class JokeDto(
        val categories: List<String>,
        @SerializedName("created_at")
        val createdAt: String,
        val id: String,
        @SerializedName("updated_at")
        val updatedAt: String,
        @SerializedName("icon_url")
        val iconUrl: String,
        val url: String,
        val value: String
    ) {
        /**
         * Converts a JokeDto into a Joke instance (the domain representation).
         */
        fun toJoke() = Joke(text = value, source = URL(url))
    }

    /**
     * The DTO used to represent a search result obtained from the remote service.
     */
    private data class SearchResultDto(val total: Int, val result: List<JokeDto>) {
        fun toJokeList() = result.map { it.toJoke() }
    }
}
