package isel.pdm.demos.tictactoe.domain.user

/**
 * Represents the user information, to be used while he is in the lobby waiting for
 * an opponent and during the game. Initialization parameters must be valid as specified by the
 * [validateUserInfoParts] function.
 * @property [nick] the user's nick name.
 * @property [motto] the user's moto, if he has one.
 */
data class UserInfo(val nick: String, val motto: String? = null) {
    init {
        require(validateUserInfoParts(nick, motto))
    }
}

/**
 * Checks whether the received values are acceptable as [UserInfo] instance fields.
 * @param [nick] the user's nick name. It cannot be blank.
 * @param [motto] the user's moto, if he has one. If present, it cannot be blank.
 * @return true if the values are acceptable, false otherwise.
 */
fun validateUserInfoParts(nick: String, motto: String?): Boolean {
    return nick.isNotBlank()
}

/**
 * Creates a [UserInfo] instance if the received values are acceptable as [UserInfo] instance fields.
 * Otherwise, returns null.
 */
fun toUserInfoOrNull(nick: String, motto: String?): UserInfo? =
    if (validateUserInfoParts(nick, motto))
        UserInfo(nick, motto)
    else
        null
