package pt.isel.pdm.nasaimageoftheday.screens.list

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import pt.isel.pdm.nasaimageoftheday.helpers.AndroidTags
import pt.isel.pdm.nasaimageoftheday.screens.components.BaseComponentActivity
import pt.isel.pdm.nasaimageoftheday.screens.detail.DetailActivity
import pt.isel.pdm.nasaimageoftheday.ui.theme.NasaImageOfTheDayTheme

class ListActivity : BaseComponentActivity<ListViewModel>() {

    companion object {
        fun navigate(activity: ComponentActivity) {
            val intent = Intent(activity, ListActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override val viewModel by viewModels<ListViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        safeSetContent {
            NasaImageOfTheDayTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ListScreen(
                        onBack = { finish() },
                        onLoadMore = { viewModel.loadMoreImages(dependencyContainer.imageService) },
                        startDate = viewModel.startDate,
                        endDate = viewModel.endDate,
                        images = viewModel.images,
                        onImageClicked = {
                            DetailActivity.navigate(this, it)
                        }
                    )
                }
            }
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.d(AndroidTags.TagName, "ListActivity.onRestoreInstanceState")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(AndroidTags.TagName, "ListActivity.onSaveInstanceState")
    }

    override fun onStart() {
        super.onStart()
        Log.d(AndroidTags.TagName, "ListActivity.onStart")
    }

    override fun onStop() {
        super.onStop()
        Log.d(AndroidTags.TagName, "ListActivity.onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(AndroidTags.TagName, "ListActivity.onDestroy")
    }
}
