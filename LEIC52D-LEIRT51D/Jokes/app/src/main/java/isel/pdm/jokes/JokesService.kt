package isel.pdm.jokes

import android.util.Log
import isel.pdm.jokes.daily.TAG
import kotlinx.coroutines.delay
import java.net.URL
import kotlin.random.Random

/**
 * Contract of the service that provides jokes
 */
interface JokesService {
    /**
     * Fetches a joke from the service
     */
    suspend fun fetchJoke(): Joke
}

/**
 * Represents an error that occurred while fetching a joke.
 * @param message The error message
 * @param cause The cause of the error, if any
 */
class FetchJokeException(message: String, cause: Throwable? = null)
    : Exception(message, cause)

/**
 * Fake implementation of the JokesService. It will replaced by a real implementation
 * in a future lecture.
 */
class FakeJokesService : JokesService {

    private val jokes = listOf(
        Joke(
            text = "Chuck Norris didn't call the wrong number, you answered the wrong phone.",
            source = URL("https://www.brainyquote.com/quotes/chuck_norris_384634")
        ),
        Joke(
            text = "The dinosaurs once looked at Chuck Norris the wrong way" +
                    " and now we call them extinct.",
            source = URL("https://www.brainyquote.com/quotes/chuck_norris_384634"),
        ),
        Joke(
            text = "Somebody asked Chuck Norris how many press ups he could do, " +
                    "Chuck Norris replied \"all of them\".",
            source = URL("https://www.brainyquote.com/quotes/chuck_norris_384634"),
        ),
    )

    override suspend fun fetchJoke(): Joke {
        Log.v(TAG, "Fetching joke")
        delay(5000)
        val index = Random.nextInt(0, jokes.size - 1)
        Log.v(TAG, "Got joke")
        return jokes[index]
    }
}

/**
 * No-op implementation of the JokesService. Defined for convenience to be used in previews.
 */
object NoOpJokeService : JokesService {
    override suspend fun fetchJoke(): Joke = Joke(text = "empty", source = URL("https://www.example.com"))
}