package isel.pdm.demos.tictactoe.ui.preferences

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.rememberCoroutineScope
import isel.pdm.demos.tictactoe.DependenciesContainer
import isel.pdm.demos.tictactoe.domain.UserInfo
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

/**
 * The activity that hosts the screen that allows the user to specify its username and moto, to be
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

    private val app by lazy { application as DependenciesContainer }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val scope = rememberCoroutineScope()
            UserPreferencesScreen(
                userInfo = userInfo,
                onSaveRequested = { userInfo ->
                    scope.launch {
                        app.userInfoRepository.updateUserInfo(userInfo)
                        finish()
                    }
                },
                onNavigateBackRequested = { finish() }
            )
        }
    }

    /**
     * Helper method to get the joke extra from the intent.
     */
    private val userInfo: UserInfo? by lazy { getUserInfoExtra()?.toUserInfo() }

    @Suppress("DEPRECATION")
    private fun getUserInfoExtra(): UserInfoExtra? =
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU)
            intent?.getParcelableExtra(USER_INFO_EXTRA, UserInfoExtra::class.java)
        else
            intent?.getParcelableExtra(USER_INFO_EXTRA)
}

private const val USER_INFO_EXTRA = "UserInfo"

/**
 * Represents the data to be passed as an extra in the intent that navigates to the
 * [UserPreferencesActivity]. We use this class because the [UserInfo] class is not
 * parcelable and we do not want to make it parcelable because it's a domain class.
 */
@Parcelize
private data class UserInfoExtra(val nick: String, val motto: String? = null) : Parcelable {
    constructor(userInfo: UserInfo) : this(userInfo.nick, userInfo.motto)
}

private fun UserInfoExtra.toUserInfo() = UserInfo(nick, motto)
