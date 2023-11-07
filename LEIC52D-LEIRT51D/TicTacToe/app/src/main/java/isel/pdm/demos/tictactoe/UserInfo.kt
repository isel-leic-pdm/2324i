package isel.pdm.demos.tictactoe

data class UserInfo(
    val nickname: String,
    val motto: String? = null
) {
    init {
        require(validateUserInfoParts(nickname, motto)) { "user info parts must not be blank" }
    }
}

fun validateUserInfoParts(nick: String, motto: String?): Boolean =
    nick.isNotBlank() && (motto == null || motto.isNotBlank())
