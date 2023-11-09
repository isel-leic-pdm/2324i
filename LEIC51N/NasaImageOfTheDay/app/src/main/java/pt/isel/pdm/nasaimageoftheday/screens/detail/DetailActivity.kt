package pt.isel.pdm.nasaimageoftheday.screens.detail

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.pdm.nasaimageoftheday.helpers.AndroidTags
import pt.isel.pdm.nasaimageoftheday.model.NasaImage
import pt.isel.pdm.nasaimageoftheday.screens.components.NasaImageView
import pt.isel.pdm.nasaimageoftheday.screens.components.NavigationHandlers
import pt.isel.pdm.nasaimageoftheday.screens.components.TopBar
import pt.isel.pdm.nasaimageoftheday.ui.theme.NasaImageOfTheDayTheme

class DetailActivity : ComponentActivity() {
    companion object {
        private const val ArgumentKey = "__Arg"
        fun navigate(ctx: ComponentActivity, image: NasaImage) {
            val intent = Intent(ctx, DetailActivity::class.java)
            intent.putExtra(ArgumentKey, image)
            ctx.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(AndroidTags.TagName, "DetailActivity.onCreate")
        val image = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            this.intent.getParcelableExtra("", NasaImage::class.java)
        else
            this.intent.getParcelableExtra<NasaImage?>(ArgumentKey)

        if (image == null) {
            finish()
            return
        }
        setContent {
            NasaImageOfTheDayTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DetailScreen(
                        image = image,
                        onBack = { finish() })
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(AndroidTags.TagName, "DetailActivity.onStart")
    }

    override fun onStop() {
        super.onStop()
        Log.d(AndroidTags.TagName, "DetailActivity.onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(AndroidTags.TagName, "DetailActivity.onDestroy")
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    image: NasaImage,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(navigationHandlers = NavigationHandlers(onBackHandler = onBack))
        }
    ) {
        Box(modifier = Modifier.padding(it))
        {
            NasaImageView(nasaImage = image)
        }
    }
}