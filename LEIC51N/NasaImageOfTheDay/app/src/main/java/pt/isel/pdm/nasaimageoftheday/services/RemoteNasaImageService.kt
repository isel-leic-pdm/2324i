package pt.isel.pdm.nasaimageoftheday.services

import android.nfc.Tag
import com.google.gson.Gson
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.Callback
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import pt.isel.pdm.nasaimageoftheday.model.NasaImage
import pt.isel.pdm.nasaimageoftheday.services.dto.NasaImageDto
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class RemoteNasaImageService(
    private val apiKey: String,
    private val baseApiUrl: String
) : NasaImageOfTheDayService {

    companion object {
        private const val ApiKeyParameter = "api_key"
        private const val TagForCancel = "RemoteNasaImageServicetagForCancel"
    }

    val httpClient by lazy {
        OkHttpClient.Builder().build()
    }

    val baseUrl by lazy {
        baseApiUrl.toHttpUrl()
            .newBuilder()
            .addQueryParameter(ApiKeyParameter, apiKey)

    }

    val gson by lazy { Gson() }

    override suspend fun getImageOfTheDay(): NasaImage {
        val request = Request
            .Builder()
            .url(baseUrl.build())
            .tag(TagForCancel)
            .build()


        return suspendCancellableCoroutine {
            it.invokeOnCancellation {
                httpClient.cancelWithTag(TagForCancel)
            }

            httpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    it.resumeWithException(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful == false || response.body == null)
                        it.resumeWithException(Exception(response.message))

                    try {
                        val dto = gson.fromJson(response.body?.string(), NasaImageDto::class.java)
                        it.resume(NasaImageDto.toNasaImage(dto))
                    } catch (e: Exception) {
                        it.resumeWithException(e)
                    }

                }
            })
        }


    }
}

private fun OkHttpClient.cancelWithTag(tag: String) {
    cancelWithTag(this.dispatcher.queuedCalls(), tag)
    cancelWithTag(this.dispatcher.runningCalls(), tag)
}

private fun cancelWithTag(calls: List<Call>, tag: String) {
    for (call in calls) {
        val tag = call.request().tag()

        if (tag != null && tag.equals(tag))
            call.cancel()
    }
}

