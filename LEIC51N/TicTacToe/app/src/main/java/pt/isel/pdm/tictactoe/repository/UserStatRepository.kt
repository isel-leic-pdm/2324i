package pt.isel.pdm.tictactoe.repository

import pt.isel.pdm.tictactoe.database.model.UserStat

interface UserStatRepository {
    suspend fun getAllStats(): List<UserStat>
    suspend fun getStat(userName: String): UserStat
    suspend fun updateStat(userStat: UserStat)
    suspend fun createStat(userName: String) : UserStat
}