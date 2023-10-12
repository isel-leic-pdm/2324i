package pt.isel.pdm.nasaimageoftheday.screens.about

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import pt.isel.pdm.nasaimageoftheday.R
import pt.isel.pdm.nasaimageoftheday.helpers.AndroidTags
import pt.isel.pdm.nasaimageoftheday.screens.about.model.SocialInfo
import pt.isel.pdm.nasaimageoftheday.ui.theme.NasaImageOfTheDayTheme

class AboutActivity : ComponentActivity() {

    companion object {
        fun navigate(source: ComponentActivity) {
            val intent = Intent(source, AboutActivity::class.java)
            source.startActivity(intent)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NasaImageOfTheDayTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AboutScreen(
                        backHandler = { this.finish() },
                        socials = socialLinks,
                        sendEmailHandler = { sendEmailIntent() },
                        socialAccountClicked = { openSocialLink(it) }
                    )
                }
            }
        }
    }

    fun sendEmailIntent() {
        try {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(
                    Intent.EXTRA_EMAIL,
                    arrayOf(AUTHOR_EMAIL)
                )
                putExtra(
                    Intent.EXTRA_SUBJECT,
                    EMAIL_SUBJECT
                )
            }

            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Log.e(AndroidTags.TagName, "Failed to send email", e)
            Toast
                .makeText(
                    this,
                    R.string.activity_info_no_suitable_app,
                    Toast.LENGTH_LONG
                )
                .show()
        }
    }

    fun openSocialLink(social: SocialInfo) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, social.link)
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Log.e(AndroidTags.TagName, "Failed to open URL", e)
            Toast
                .makeText(
                    this,
                    R.string.activity_info_no_suitable_app,
                    Toast.LENGTH_LONG
                )
                .show()
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(AndroidTags.TagName, "AboutActivity.onStart")
    }

    override fun onStop() {
        super.onStop()
        Log.d(AndroidTags.TagName, "AboutActivity.onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(AndroidTags.TagName, "AboutActivity.onDestroy")
    }
}


private const val AUTHOR_EMAIL = "diogo.cardoso@isel.pt"
private const val EMAIL_SUBJECT = "About the Nasa Images"

val socialLinks = listOf(
    SocialInfo(
        link = Uri.parse("https://www.linkedin.com/in/diogocardoso89/"),
        imageId = R.drawable.ic_linkedin
    ),
    SocialInfo(
        link = Uri.parse("https://www.youtube.com/imaginationoverflow"),
        imageId = R.drawable.ic_youtube
    ),
    SocialInfo(
        link = Uri.parse("https://discord.com/users/372168267898421269"),
        imageId = R.drawable.ic_discord
    ),
    SocialInfo(
        link = Uri.parse("https://imaginationoverflow.com/"),
        imageId = R.drawable.ic_home
    )
)