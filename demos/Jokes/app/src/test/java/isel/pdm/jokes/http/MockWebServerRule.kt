package isel.pdm.jokes.http

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.rules.TestWatcher
import org.junit.runner.Description

const val CALL_TIMEOUT_IN_SECS = 10L

/**
 * A JUnit rule that starts (and shuts down) a mock web to be used in tests.
 */
class MockWebServerRule : TestWatcher() {

    val webServer = MockWebServer()
    val httpClient: OkHttpClient =
        OkHttpClient.Builder()
            .callTimeout(CALL_TIMEOUT_IN_SECS, java.util.concurrent.TimeUnit.SECONDS)
            .build()
    val gson: Gson = Gson()

    override fun starting(description: Description) {
        super.starting(description)
        webServer.start()
    }

    override fun finished(description: Description) {
        super.finished(description)
        webServer.shutdown()
    }
}
