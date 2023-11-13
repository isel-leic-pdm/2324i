package isel.pdm.demos.tictactoe

import isel.pdm.demos.tictactoe.domain.UserInfo
import org.junit.Test

class UserInfoTests {

    @Test
    fun create_user_info_with_non_blank_nickname_and_motto_succeeds() {
        UserInfo("nickname", "motto")
    }

    @Test(expected = IllegalArgumentException::class)
    fun create_user_info_with_blank_nickname_fails() {
        UserInfo("", "motto")
    }
}