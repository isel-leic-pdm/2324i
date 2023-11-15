package isel.pdm.demos.tictactoe.ui.common.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import isel.pdm.demos.tictactoe.R

// https://www.fontspace.com/rusty-hooks-font-f87577
private val TicTacToe = FontFamily(
    Font(R.font.rusty_hooks)
)

// Set of Material typography styles to start with
// For more information see https://m3.material.io/styles/typography/type-scale-tokens
val Typography = Typography(
    displayMedium = TextStyle(
        fontFamily = TicTacToe,
        fontWeight = FontWeight.Bold,
        letterSpacing = 2.sp,
        fontSize = 40.sp
    ),

    headlineMedium = TextStyle(
        fontFamily = TicTacToe,
        fontWeight = FontWeight.Normal,
        letterSpacing = 2.sp,
        fontSize = 24.sp
    ),

    titleMedium = TextStyle(
        fontFamily = TicTacToe,
        fontWeight = FontWeight.Normal,
        letterSpacing = 2.sp,
        fontSize = 18.sp
    ),

    labelMedium = TextStyle(
        fontFamily = TicTacToe,
        fontWeight = FontWeight.Normal,
        letterSpacing = 2.sp,
        fontSize = 24.sp
    ),
)