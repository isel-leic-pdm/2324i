package pt.isel.pdm.tictactoe.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pt.isel.pdm.tictactoe.R
import pt.isel.pdm.tictactoe.model.Cell
import pt.isel.pdm.tictactoe.ui.components.TicTacToeBoard

@Composable
fun HomeScreen(
    list: List<Cell>,
    onStartClicked: () -> Unit,
) {
    Box {
        Box(contentAlignment = Alignment.Center) {
            TicTacToeBoard(list, {})
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background.copy(alpha = .70f))
            )
        }

        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.displayLarge,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(24.dp)

        )

        Button(
            modifier = Modifier
                .padding(24.dp)
                .align(Alignment.BottomCenter),
            onClick = onStartClicked
        ) {
            Text(
                text = stringResource(id = R.string.start),
                style = MaterialTheme.typography.displaySmall,
                modifier = Modifier.padding(12.dp)

            )
        }
    }
}
