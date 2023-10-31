package isel.pdm.jokes.domain

import kotlinx.coroutines.delay
import java.net.URL
import kotlin.random.Random

/**
 * Contract of the service that provides jokes.
 */
interface JokesService {
    /**
     * Fetches a joke.
     * @return The fetched joke.
     * @throws FetchJokeException If the joke could not be fetched.
     * @throws kotlinx.coroutines.CancellationException If the operation was cancelled.
     */
    suspend fun fetchJoke(): Joke

    /**
     * Fetches a list of jokes that match the given term.
     * @param term The term to search for.
     * @throws FetchJokeException If the jokes could not be fetched.
     * @throws kotlinx.coroutines.CancellationException If the operation was cancelled.
     */
    suspend fun searchJokes(term: Term): List<Joke>
}

/**
 * Represents an error that occurred while fetching a joke.
 * @param message The error message
 * @param cause The cause of the error, if any
 */
class FetchJokeException(message: String, cause: Throwable? = null)
    : Exception(message, cause)

/**
 * Fake implementation of the JokesService. It returns a random joke from a pre-established list
 * of jokes.
 */
@Suppress("unused")
class FakeJokesService : JokesService {

    companion object {
        val jokes = listOf(
            Joke(
                text = "Chuck Norris didn't call the wrong number, you answered the wrong phone.",
                source = URL("https://www.keeplaughingforever.com/chuck-norris-jokes")
            ),
            Joke(
                text = "The dinosaurs once looked at Chuck Norris the wrong way" +
                        " and now we call them extinct.",
                source = URL("https://www.keeplaughingforever.com/chuck-norris-jokes"),
            ),
            Joke(
                text = "Somebody asked Chuck Norris how many press ups he could do, " +
                        "Chuck Norris replied \"all of them\".",
                source = URL("https://www.keeplaughingforever.com/chuck-norris-jokes"),
            ),
            Joke(
                text = "This graveyard looks overcrowded. People must be dying to get in there.",
                source = URL("https://www.keeplaughingforever.com/corny-dad-jokes")
            ),
            Joke(
                text = "Chuck Norris didn't call the wrong number, you answered the wrong phone.",
                source = URL("https://www.keeplaughingforever.com/chuck-norris-jokes")
            ),
            Joke(
                text = "The dinosaurs once looked at Chuck Norris the wrong way" +
                        " and now we call them extinct.",
                source = URL("https://www.keeplaughingforever.com/chuck-norris-jokes"),
            ),
            Joke(
                text = "Somebody asked Chuck Norris how many press ups he could do, " +
                        "Chuck Norris replied \"all of them\".",
                source = URL("https://www.keeplaughingforever.com/chuck-norris-jokes"),
            ),
            Joke(
                text = "This graveyard looks overcrowded. People must be dying to get in there.",
                source = URL("https://www.keeplaughingforever.com/corny-dad-jokes")
            ),
            Joke(
                text = "Chuck Norris didn't call the wrong number, you answered the wrong phone.",
                source = URL("https://www.keeplaughingforever.com/chuck-norris-jokes")
            ),
            Joke(
                text = "The dinosaurs once looked at Chuck Norris the wrong way" +
                        " and now we call them extinct.",
                source = URL("https://www.keeplaughingforever.com/chuck-norris-jokes"),
            ),
            Joke(
                text = "Somebody asked Chuck Norris how many press ups he could do, " +
                        "Chuck Norris replied \"all of them\".",
                source = URL("https://www.keeplaughingforever.com/chuck-norris-jokes"),
            ),
            Joke(
                text = "This graveyard looks overcrowded. People must be dying to get in there.",
                source = URL("https://www.keeplaughingforever.com/corny-dad-jokes")
            ),
        )
    }

    override suspend fun fetchJoke(): Joke {
        delay(3000)
        val index = Random.nextInt(from = 0, until = jokes.size)
        return jokes[index]
    }

    override suspend fun searchJokes(term: Term): List<Joke> {
        delay(3000)
        return jokes
    }
}

/**
 * Represents search terms. A term is a string with between 3 and 120 characters. White spaces
 * at the beginning and end of the term are ignored.
 * @property value The term's text.
 */
@JvmInline
value class Term(val value: String) {
    init {
        require(isValidTerm(value)) { "Term must have between 3 and 120 characters" }
    }
}

/**
 * Checks if the given string is a valid term.
 * @param termWannabe The string to check.
 * @return `true` if the given string is a valid term, `false` otherwise.
 */
fun isValidTerm(termWannabe: String): Boolean = termWannabe.trim().let {
    it.isNotBlank() && it.length in 3 until 120
}

/**
 * Extension method that converts this string into a [Term] instance.
 * @return The [Term] instance or `null` if this string is not a valid term.
 */
fun String.toTermOrNull(): Term? = if (isValidTerm(this)) Term(this) else null