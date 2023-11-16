package pt.isel.pdm.tictactoe.repository

import kotlinx.coroutines.delay
import pt.isel.pdm.tictactoe.model.UserInfo

class FakeUserRepository : UserRepository {
    private var user: UserInfo? = null
    override suspend fun getUserData(): UserInfo? {
        delay(1000)
        return UserInfo("asd")
    }

    override suspend fun setUserData(usr: UserInfo) {
        delay(1000)
        user = usr
    }
}