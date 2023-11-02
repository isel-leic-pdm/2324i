package pt.isel.pdm.nasaimageoftheday.services

import android.nfc.Tag
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import pt.isel.pdm.nasaimageoftheday.model.NasaImage
import pt.isel.pdm.nasaimageoftheday.services.dto.NasaImageDto
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class RemoteNasaImageService(
    private val apiKey: String,
    private val baseApiUrl: String
) : NasaImageOfTheDayService {

    companion object {
        private const val ApiKeyParameter = "api_key"
        private const val DateParameter = "date"
        private const val StartDateParameter = "start_date"
        private const val EndDateParameter = "end_date"
        private const val TagForCancel = "RemoteNasaImageServicetagForCancel"
    }

    val httpClient by lazy {
        OkHttpClient.Builder().build()
    }

    val baseUrl = {
        baseApiUrl.toHttpUrl()
            .newBuilder()
            .addQueryParameter(ApiKeyParameter, apiKey)
    }

    val gson by lazy { Gson() }

    private val listDeserializationType = object : TypeToken<List<NasaImageDto>>() {}.type

    override suspend fun getImageOfTheDay(): NasaImage {
        return getImageOf(LocalDate.now())
    }

    override suspend fun getImageOf(date: LocalDate): NasaImage {
        return DoRequest(
            baseUrl().addQueryParameter(DateParameter, getFormattedDate(date))
        ) {
            val dto = gson.fromJson(it.body?.string(), NasaImageDto::class.java)
            return@DoRequest NasaImageDto.toNasaImage(dto)
        }
    }

    override suspend fun getImages(startDate: LocalDate, endDate: LocalDate): List<NasaImage> {
        return DoRequest(
            baseUrl()
                .addQueryParameter(StartDateParameter, getFormattedDate(startDate))
                .addQueryParameter(EndDateParameter, getFormattedDate(endDate))
        ) {
            val dto = gson.fromJson<List<NasaImageDto>>(it.body?.string(), listDeserializationType)
            return@DoRequest dto.map { NasaImageDto.toNasaImage(it) }
        }

    }

    private suspend fun <T> DoRequest(
        url: HttpUrl.Builder,
        validateBody: Boolean = true,
        callback: (Response) -> T
    ): T {
        val request = Request
            .Builder()
            .url(url.build())
            .build()

        val httpCall = httpClient.newCall(request);

        return suspendCancellableCoroutine {
            it.invokeOnCancellation {
                httpCall.cancel()
            }

            httpCall.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    it.resumeWithException(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (validateBody && (response.isSuccessful == false || response.body == null))
                        it.resumeWithException(Exception(response.message))

                    try {
                        it.resume(callback(response))
                    } catch (e: Exception) {
                        it.resumeWithException(e)
                    }

                }
            })
        }
    }


    private fun getFormattedDate(d: LocalDate): String {
        return d.format(DateTimeFormatter.ISO_LOCAL_DATE)
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

