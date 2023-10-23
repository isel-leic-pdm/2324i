package isel.pdm.jokes

import android.app.Application
import com.google.gson.Gson
import isel.pdm.jokes.http.IcanhazDadJoke
import okhttp3.OkHttpClient

class JokesApplication : Application() {

    val gson = Gson()

    val client =
        OkHttpClient.Builder()
            .build()

    val service = IcanhazDadJoke(client, gson)
}