package pt.isel.pdm.tictactoe.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import pt.isel.pdm.tictactoe.DependencyContainer
import pt.isel.pdm.tictactoe.R
import pt.isel.pdm.tictactoe.ui.components.ErrorMessage
import pt.isel.pdm.tictactoe.ui.home.HomeActivity

abstract class BaseActivity<T> : ComponentActivity() where T : BaseViewModel {
    protected val dependencyContainer by lazy { (application as DependencyContainer) }
    protected abstract val viewModel: T

    protected val activityTag: String = this.javaClass.simpleName

    protected fun safeSetContent(content: @Composable () -> Unit) {
        setContent {
            content()
            LoadingAndError(viewModel.isLoading, viewModel.error)
        }
    }

    protected inline fun <reified A> navigate(
        noinline apply: ((Intent) -> Unit)? = null
    ) {
        HomeActivity::class.java
        val intent = Intent(this, A::class.java)

        if (apply != null)
            apply(intent)

        this.startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        Log.d(activityTag, "onCreate")
    }

    override fun onStart() {
        super.onStart()
        Log.d(activityTag, "onStart")
    }

    override fun onStop() {
        super.onStop()
        Log.d(activityTag,"onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(activityTag, "onDestroy")
    }


}

@Composable
private fun LoadingAndError(loading: Boolean, error: String?) {
    if (loading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White.copy(alpha = .8f))
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(id = R.string.loading)
            )
        }
    }

    if (error != null)
        ErrorMessage(error)
}