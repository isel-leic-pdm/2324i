package pt.isel.pdm.tictactoe.repository

import pt.isel.pdm.tictactoe.model.UserInfo


interface UserRepository {
    suspend fun getUserData(): UserInfo?
    suspend fun setUserData(usr: UserInfo)
}

