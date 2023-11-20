package isel.pdm.demos.tictactoe.ui.main

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
 * The tag used to identify the main screen.
 */
const val MainScreenTag = "MainScreen"

/**
 * The tag used to identify the main screen's button used to start the game.
 */
const val PlayButtonTag = "PlayButton"


/**
 * The application's main screen.
 * @param onPlayRequested The callback to invoke when the user requests to play a game.
 */
@Composable
fun MainScreen(
    onPlayEnabled: Boolean = true,
    onPlayRequested: () -> Unit
) {
    TicTacToeTheme {
        Surface(
            modifier = Modifier.fillMaxSize().testTag(MainScreenTag)
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
                    enabled = onPlayEnabled,
                    onClick = onPlayRequested,
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier.testTag(PlayButtonTag)
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
fun MainScreenPreview() {
    MainScreen(onPlayRequested = { })
}