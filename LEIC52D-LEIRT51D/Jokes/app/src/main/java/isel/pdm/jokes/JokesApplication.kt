package isel.pdm.jokes

import android.app.Application
import com.google.gson.Gson
import isel.pdm.jokes.http.IcanhazDadJoke
import okhttp3.OkHttpClient

class JokesApplication : Application() {

    val httpClient: OkHttpClient =
        OkHttpClient.Builder()
            .build()

    val gson: Gson = Gson()

    val jokesService: JokesService = IcanhazDadJoke(httpClient, gson)

}