package isel.pdm.jokes.http

import isel.pdm.jokes.domain.Joke
import isel.pdm.jokes.domain.JokesService
import isel.pdm.jokes.domain.Term

/**
 * Actual implementation of the JokesService, which fetches jokes from the pre-configured
 * set of remote joke providers.
 * @param providers The list of joke providers
 */
class JokesServiceImpl(
    private val providers: List<JokesService>
) : JokesService {

    override suspend fun fetchJoke(): Joke {
        val index = providers.indices.random()
        return providers[index].fetchJoke()
    }

    override suspend fun searchJokes(term: Term): List<Joke> {
        val index = providers.indices.random()
        return providers[index].searchJokes(term)
    }
}