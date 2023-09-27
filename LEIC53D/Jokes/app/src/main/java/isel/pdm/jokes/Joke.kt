package isel.pdm.jokes

import java.net.URL

/**
 * Represents a joke.
 * @property text The joke's text
 * @property source The URL of the source of the joke
 */
data class Joke(val text: String, val source: URL) {
    init {
        require(text.isNotBlank()) { "The joke's text must not be blank" }
    }
}
