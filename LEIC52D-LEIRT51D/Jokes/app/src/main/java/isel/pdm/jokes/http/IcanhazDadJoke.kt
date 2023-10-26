package isel.pdm.jokes.http

import com.google.gson.Gson
import isel.pdm.jokes.FetchJokeException
import isel.pdm.jokes.Joke
import isel.pdm.jokes.JokesService
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.IOException
import java.net.URL
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class IcanhazDadJoke(
    private val client: OkHttpClient,
    private val gson: Gson
) : JokesService {

    private val request by lazy {
        Request.Builder()
            .url("https://icanhazdadjoke.com/")
            .addHeader("accept", "application/json")
            .build()
    }

    override suspend fun fetchJoke(): Joke {
        return suspendCoroutine {
            client.newCall(request).enqueue(object : okhttp3.Callback {
                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    it.resumeWithException(FetchJokeException("Failed to fetch joke", e))
                }

                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    val body = response.body
                    if (!response.isSuccessful || body == null)
                        it.resumeWithException(FetchJokeException("Failed to fetch joke: ${response.code}"))
                    else
                        it.resume(gson.fromJson(body.string(), JokeDto::class.java).toJoke())
                }
            })
        }

//        return withContext(Dispatchers.IO) {
//            client.newCall(request).execute().use {
//                val body = it.body
//                if (!it.isSuccessful || body == null)
//                    throw FetchJokeException("Failed to fetch joke: ${it.code}")
//                gson.fromJson(body.string(), JokeDto::class.java).toJoke()
//            }
//        }
    }
}

private data class JokeDto(
    val id: String,
    val joke: String,
    val status: Int
) {
    fun toJoke() = Joke(text = joke, source = URL("https://icanhazdadjoke.com/"))
}


