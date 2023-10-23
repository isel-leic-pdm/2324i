package isel.pdm.jokes.http

import com.google.gson.Gson
import isel.pdm.jokes.FetchJokeException
import isel.pdm.jokes.Joke
import isel.pdm.jokes.JokesService
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.net.URL
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class IcanhazDadJoke(
    private val client: OkHttpClient,
    private val gson: Gson
) : JokesService {

    private val request: Request by lazy {
        Request.Builder()
            .url("https://icanhazdadjoke.com/")
            .addHeader("accept", "application/json")
            .build()
    }

    override suspend fun fetchJoke(): Joke =
        suspendCoroutine {
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    it.resumeWithException(FetchJokeException("Could not fetch joke", e))
                }

                override fun onResponse(call: Call, response: Response) {
                    val body = response.body
                    if (!response.isSuccessful || body == null)
                        it.resumeWithException(FetchJokeException("Could not fetch joke. Remote service returned ${response.code}"))
                    else {
                        val dto = gson.fromJson(body.string(), JokeDto::class.java)
                        it.resumeWith(Result.success(dto.toJoke()))
                    }
                }
            })
        }

    private data class JokeDto(
        val id: String,
        val joke: String,
        val status: Int
    ) {
        fun toJoke() = Joke(text = joke, source = URL("https://icanhazdadjoke.com/"))
    }
}