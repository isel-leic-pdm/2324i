package isel.pdm.demos.tictactoe

data class UserInfo(
    val nickname: String,
    val motto: String?
) {
    init {
        require(validateUserInfoParts(nickname, motto)) { "The user info parts must not be blank" }
    }
}

fun validateUserInfoParts(nickname: String, motto: String?) =
    nickname.isNotBlank() && motto?.isNotBlank() ?: true