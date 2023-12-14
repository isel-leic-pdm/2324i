package isel.pdm.demos.tictactoe.ui.game.lobby

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import isel.pdm.demos.tictactoe.domain.game.lobby.PlayerInfo
import isel.pdm.demos.tictactoe.domain.user.UserInfo
import isel.pdm.demos.tictactoe.ui.common.theme.TicTacToeTheme

const val PlayerInfoViewTag = "PlayerInfoView"

@Composable
fun PlayerInfoView(
    playerInfo: PlayerInfo,
    onPlayerSelected: (PlayerInfo) -> Unit
) {
    Card(
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onPlayerSelected(playerInfo) }
            .testTag(PlayerInfoViewTag)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = playerInfo.info.nick,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Start,
                maxLines = 1,
                modifier = Modifier.fillMaxWidth()
            )
            if (playerInfo.info.motto != null) {
                Text(
                    text = playerInfo.info.motto,
                    style = MaterialTheme.typography.labelLarge,
                    textAlign = TextAlign.Start,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, end = 8.dp),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun UserInfoViewNoMotoPreview() {
    TicTacToeTheme {
        PlayerInfoView(
            playerInfo = PlayerInfo(UserInfo("My Nick")),
            onPlayerSelected = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UserInfoViewPreview() {
    TicTacToeTheme {
        PlayerInfoView(
            playerInfo = PlayerInfo(UserInfo("My Nick", "This is my moto")),
            onPlayerSelected = { }
        )
    }
}
