package pt.isel.pdm.tictactoe.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameInfo(
    val gameId: String,
    val isPlayer1: Boolean
) : Parcelable{
}