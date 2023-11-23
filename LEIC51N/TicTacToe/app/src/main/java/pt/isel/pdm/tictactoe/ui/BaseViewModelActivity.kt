package pt.isel.pdm.tictactoe.ui

import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import pt.isel.pdm.tictactoe.ui.components.ErrorMessageComponent
import pt.isel.pdm.tictactoe.ui.components.LoadingMessageComponent

abstract class BaseViewModelActivity<T> : BaseActivity() where T : BaseViewModel {
    protected abstract val viewModel: T
    protected fun safeSetContent(content: @Composable () -> Unit) {
        setContent {
            content()
            LoadingAndError(viewModel.isLoading, viewModel.error)
        }
    }
}

@Composable
private fun LoadingAndError(loading: Boolean, error: String?) {
    if (loading) {
        LoadingMessageComponent()
    }

    if (error != null)
        ErrorMessageComponent(error)
}

