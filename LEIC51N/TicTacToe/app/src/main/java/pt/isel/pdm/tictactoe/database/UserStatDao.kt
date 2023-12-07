package pt.isel.pdm.tictactoe.database

import androidx.room.*
import pt.isel.pdm.tictactoe.database.model.UserStat

@Dao
interface UserStatDao {
    @Query("SELECT * FROM userStat")
    fun getAll(): List<UserStat>

    @Query("SELECT * FROM userStat WHERE opponentName LIKE :userName LIMIT 1")
    fun findByName(userName: String): UserStat

    @Insert
    fun insertAll(user: UserStat)

    @Update
    fun update(user: UserStat)

    @Delete
    fun delete(user: UserStat)

}