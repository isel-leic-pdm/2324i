package pt.isel.pdm.tictactoe.database

import androidx.room.Database
import androidx.room.RoomDatabase
import pt.isel.pdm.tictactoe.database.model.UserStat

@Database(entities = [UserStat::class], version = 1)
abstract class TicTacToeDatabase : RoomDatabase() {
    abstract fun userStatDao(): UserStatDao
}