package isel.pdm.demos.tictactoe.domain

import isel.pdm.demos.tictactoe.domain.UserInfo

interface UserInfoRepository {
    suspend fun getUserInfo(): UserInfo?
    suspend fun updateUserInfo(userInfo: UserInfo)
}
