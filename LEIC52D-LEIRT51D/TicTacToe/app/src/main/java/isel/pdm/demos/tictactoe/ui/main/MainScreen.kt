package isel.pdm.demos.tictactoe.ui.main

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import isel.pdm.demos.tictactoe.R
import isel.pdm.demos.tictactoe.ui.common.theme.TicTacToeTheme

/**
 * The tag used to identify the start game button.
 */
const val PlayButtonTag = "PlayButton"

/**
 * The application's main screen.
 * @param onPlayRequested the callback to invoke when the user requests to start a game.
 */
@Composable
fun MainScreen(onPlayRequested: () -> Unit) {
    TicTacToeTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.background(MaterialTheme.colorScheme.background)
            ) {
                Text(
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                )
                Image(
                    painter = painterResource(id = R.drawable.im_tic_tac_toe),
                    contentDescription = "",
                    modifier = Modifier.sizeIn(
                        minWidth = 80.dp,
                        minHeight = 80.dp,
                        maxWidth = 200.dp,
                        maxHeight = 200.dp
                    )
                )

                Button(
                    onClick = onPlayRequested,
                    modifier = Modifier.testTag(PlayButtonTag),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = stringResource(id = R.string.start_game_button_text),
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(all = 4.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreviewLight() {
    MainScreen(onPlayRequested = { })
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MainScreenPreviewDark() {
    MainScreen(onPlayRequested = { })
}
