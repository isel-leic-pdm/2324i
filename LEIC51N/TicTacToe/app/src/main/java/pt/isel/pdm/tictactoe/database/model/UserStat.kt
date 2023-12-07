package pt.isel.pdm.tictactoe.database.model

import androidx.room.*


@Entity
data class UserStat(
    @PrimaryKey val opponentName: String,
    val wins : Int,
    val draws : Int,
    val losses : Int,
)
