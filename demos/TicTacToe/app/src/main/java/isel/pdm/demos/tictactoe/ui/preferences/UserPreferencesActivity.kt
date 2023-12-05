package isel.pdm.demos.tictactoe.ui.preferences

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.lifecycleScope
import isel.pdm.demos.tictactoe.DependenciesContainer
import isel.pdm.demos.tictactoe.R
import isel.pdm.demos.tictactoe.domain.Saved
import isel.pdm.demos.tictactoe.domain.getOrNull
import isel.pdm.demos.tictactoe.domain.idle
import isel.pdm.demos.tictactoe.domain.user.UserInfo
import isel.pdm.demos.tictactoe.ui.common.ErrorAlert
import isel.pdm.demos.tictactoe.ui.common.UserInfoExtra
import isel.pdm.demos.tictactoe.ui.common.toUserInfo
import kotlinx.coroutines.launch

/**
 * The name of the extra that contains the user information.
 */
private const val USER_INFO_EXTRA = "UserInfo"

/**
 * The activity that hosts the screen that allows the user to specify its nickname and moto, to be
 * used in the lobby and during the game.
 */
class UserPreferencesActivity : ComponentActivity() {

    companion object {
        /**
         * Navigates to the [UserPreferencesActivity] activity.
         * @param ctx the context to be used.
         */
        fun navigateTo(ctx: Context, userInfo: UserInfo? = null) {
            ctx.startActivity(createIntent(ctx, userInfo))
        }

        /**
         * Builds the intent that navigates to the [UserPreferencesActivity] activity.
         * @param ctx the context to be used.
         */
        fun createIntent(ctx: Context, userInfo: UserInfo? = null): Intent {
            val intent = Intent(ctx, UserPreferencesActivity::class.java)
            userInfo?.let { intent.putExtra(USER_INFO_EXTRA, UserInfoExtra(it)) }
            return intent
        }
    }

    private val vm by viewModels<UserPreferencesScreenViewModel> {
        UserPreferencesScreenViewModel.factory((application as DependenciesContainer).userInfoRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            vm.ioState.collect {
                if (it is Saved && it.value.isSuccess)
                    finish()
            }
        }

        setContent {
            val ioState by vm.ioState.collectAsState(initial = idle())
            UserPreferencesScreen(
                userInfo = ioState.getOrNull() ?: userInfoExtra,
                onSaveRequested = { userInfoToSave -> vm.updateUserInfo(userInfoToSave) },
                onNavigateBackRequested = { finish() }
            )

            ioState.let {
                if (it is Saved && it.value.isFailure)
                    ErrorAlert(
                        title = R.string.failed_to_save_preferences_error_dialog_title,
                        message = R.string.failed_to_save_preferences_error_dialog_text,
                        buttonText = R.string.failed_to_save_preferences_error_dialog_retry_button,
                        onDismiss = { vm.resetToIdle() }
                    )
            }
        }
    }

    /**
     * Helper method to get the user info extra from the intent.
     */
    private val userInfoExtra: UserInfo? by lazy { getUserInfoExtra()?.toUserInfo() }

    @Suppress("DEPRECATION")
    private fun getUserInfoExtra(): UserInfoExtra? =
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU)
            intent.getParcelableExtra(USER_INFO_EXTRA, UserInfoExtra::class.java)
        else
            intent.getParcelableExtra(USER_INFO_EXTRA)
}
