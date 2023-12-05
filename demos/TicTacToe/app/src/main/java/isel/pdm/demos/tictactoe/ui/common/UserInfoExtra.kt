package isel.pdm.demos.tictactoe.ui.common

import android.os.Parcelable
import isel.pdm.demos.tictactoe.domain.user.UserInfo
import kotlinx.parcelize.Parcelize

/**
 * Represents the data to be passed as an extra in the intents used to navigate to activities
 * that require the user information. We use this class because the [UserInfo] class is not
 * parcelable and we do not want to make it parcelable because it's a domain class.
 */
@Parcelize
data class UserInfoExtra(val nick: String, val motto: String? = null) : Parcelable {
    constructor(userInfo: UserInfo) : this(userInfo.nick, userInfo.motto)
}

/**
 * Converts this [UserInfoExtra] to a [UserInfo].
 */
fun UserInfoExtra.toUserInfo() = UserInfo(nick, motto)