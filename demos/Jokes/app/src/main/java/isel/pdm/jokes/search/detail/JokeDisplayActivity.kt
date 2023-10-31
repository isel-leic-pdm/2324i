package isel.pdm.jokes.search.detail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import isel.pdm.jokes.domain.Joke
import kotlinx.parcelize.Parcelize
import java.net.URL

/**
 * Activity used to display a joke.
 *
 * Navigation to this activity is done through the [JokeDisplayActivity.navigate] method.
 * The joke is passed as an extra in the intent. The [Joke] class is not parcelable and we do not
 * want to make it parcelable because it is a domain class. So we use the [JokeExtra] class instead.
 */
class JokeDisplayActivity : ComponentActivity() {

    companion object {
        /**
         * Navigates to the [JokeDisplayActivity] activity.
         * @param origin the activity from which the navigation is performed.
         * @param joke the joke to be displayed.
         */
        fun navigate(origin: Activity, joke: Joke) {
            with(origin) {
                val intent = Intent(this, JokeDisplayActivity::class.java)
                intent.putExtra(JOKE_EXTRA, JokeExtra(joke))
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            getJokeExtra()?.let { jokeExtra ->
                JokeDisplayScreen(
                    joke = jokeExtra.toJoke(),
                    onNavigateBack = { finish() }
                )
            }
        }
    }

    /**
     * Helper method to get the joke extra from the intent.
     */
    @Suppress("DEPRECATION")
    private fun getJokeExtra(): JokeExtra? =
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU)
            intent.getParcelableExtra(JOKE_EXTRA, JokeExtra::class.java)
        else
            intent.getParcelableExtra(JOKE_EXTRA)
}

private const val JOKE_EXTRA = "JokeDisplayActivity.extra.Joke"

/**
 * Represents the data to be passed as an extra in the intent that navigates to the
 * [JokeDisplayActivity] activity. We use this class because the [Joke] class is not
 * parcelable and we do not want to make it parcelable because it is a domain class.
 *
 * @property text the joke's text.
 * @property source the joke's source.
 * @constructor Creates an instance of [JokeExtra] from a [Joke].
 */
@Parcelize
private data class JokeExtra(val text: String, val source: String) : Parcelable {
    constructor(joke: Joke) : this(joke.text, joke.source.toString())
}

/**
 * Helper method to convert a [JokeExtra] to a [Joke].
 */
private fun JokeExtra.toJoke() = Joke(text, URL(source))
