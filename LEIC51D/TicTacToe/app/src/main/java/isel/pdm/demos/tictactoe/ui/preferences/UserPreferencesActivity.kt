package isel.pdm.demos.tictactoe.ui.preferences

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import isel.pdm.demos.tictactoe.domain.UserInfo

class UserPreferencesActivity : ComponentActivity() {

    companion object {
        fun navigateTo(ctx: Context, userInfo: UserInfo) {
            TODO()
        }

        fun createIntent(ctx: Context, userInfo: UserInfo): Intent {
            TODO()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UserPreferencesScreen(
                userInfo = null,
                onSaveRequested = { _ -> TODO() },
                onNavigateBackRequested = { TODO() }
            )
        }
    }
}