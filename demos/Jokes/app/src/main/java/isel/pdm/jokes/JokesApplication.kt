package isel.pdm.jokes

import android.app.Application
import com.google.gson.Gson
import isel.pdm.jokes.domain.FakeJokesService
import isel.pdm.jokes.domain.JokesService
import isel.pdm.jokes.http.JokesServiceImpl
import isel.pdm.jokes.http.providers.IcanhazDadJoke
import isel.pdm.jokes.http.providers.MatChillingChuckNorris
import okhttp3.OkHttpClient

/**
 * The tag used to identify log messages across the application. Here we elected to use the same
 * tag for all log messages.
 */
const val TAG = "JOKES_APP_TAG"

/**
 * The contract to be supported by the application's class used to resolve dependencies.
 */
interface JokesDependencyProvider {
    /**
     * The HTTP client used to perform HTTP requests
     */
    val httpClient: OkHttpClient

    /**
     * The JSON serializer/deserializer used to convert JSON into DTOs
     */
    val gson: Gson

    /**
     * The service used to fetch jokes
     */
    val jokesService: JokesService
}

/**
 * The application's class used to resolve dependencies, acting as a Service Locator.
 * Dependencies are then injected manually by each Android Component (e.g Activity, Service, etc.).
 */
class JokesApplication : Application(), JokesDependencyProvider {
    /**
     * The HTTP client used to perform HTTP requests
     */
    override val httpClient: OkHttpClient =
        OkHttpClient.Builder()
            .callTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
            .build()

    /**
     * The JSON serializer/deserializer used to convert JSON into DTOs
     */
    override val gson: Gson = Gson()

    /**
     * The service used to fetch jokes
     */
    override val jokesService: JokesService =
        JokesServiceImpl(
            listOf(
                MatChillingChuckNorris(httpClient, gson),
                IcanhazDadJoke(httpClient, gson)
            )
        )
}