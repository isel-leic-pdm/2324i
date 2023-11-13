package isel.pdm.demos.tictactoe.domain

interface UserInfoRepository {
    suspend fun getUserInfo(): UserInfo?
    suspend fun saveUserInfo(userInfo: UserInfo)
}