package pt.isel.pdm.tictactoe.database

import androidx.room.*
import pt.isel.pdm.tictactoe.database.model.UserStat

@Dao
interface UserStatDao {
    @Query("SELECT * FROM userStat")
    suspend fun getAll(): List<UserStat>

    @Query("SELECT * FROM userStat WHERE opponentName LIKE :userName")
    suspend fun findByName(userName: String): UserStat?

    @Insert
    suspend fun insert(user: UserStat)

    @Update
    suspend fun update(user: UserStat)

    @Delete
    suspend fun delete(user: UserStat)

}