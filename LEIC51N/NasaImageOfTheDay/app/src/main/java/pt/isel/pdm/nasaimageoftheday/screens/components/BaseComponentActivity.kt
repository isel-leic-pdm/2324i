package pt.isel.pdm.nasaimageoftheday.screens.components

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import pt.isel.pdm.nasaimageoftheday.NasaApplication

abstract class BaseComponentActivity<T> : ComponentActivity() where T : BaseViewModel {

    abstract val viewModel: T
    val dependencyContainer by lazy { (this.application as NasaApplication) }
    fun safeSetContent(screen: @Composable () -> Unit) {
        setContent {
            screen()
            LoadingScreen(viewModel.isLoading)
            ErrorMessage(viewModel.error)
        }
    }
}