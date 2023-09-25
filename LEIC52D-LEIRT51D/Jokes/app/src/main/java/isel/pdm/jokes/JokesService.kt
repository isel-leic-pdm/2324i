package isel.pdm.jokes

import java.net.URL

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

    override suspend fun fetchJoke(): Joke = jokes[0]
}

/**
 * No-op implementation of the JokesService. Defined for convenience to be used in previews.
 */
object NoOpJokeService : JokesService {
    override suspend fun fetchJoke(): Joke = Joke(text = "empty", source = URL("https://www.example.com"))
}