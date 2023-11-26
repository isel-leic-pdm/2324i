package isel.pdm.demos.tictactoe.ui.common

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import isel.pdm.demos.tictactoe.ui.common.theme.TicTacToeTheme

const val ErrorAlertTestTag = "ErrorAlertTestTag"

@Composable
fun ErrorAlert(
    @StringRes title: Int,
    @StringRes message: Int,
    @StringRes buttonText: Int,
    onDismiss: () -> Unit = { }
) {
    ErrorAlertImpl(
        title = stringResource(id = title),
        message = stringResource(id = message),
        buttonText = stringResource(id = buttonText),
        onDismiss = onDismiss
    )
}

@Composable
private fun ErrorAlertImpl(
    title: String,
    message: String,
    buttonText: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { },
        confirmButton = {
            OutlinedButton(
                border = BorderStroke(0.dp, Color.Unspecified),
                onClick = onDismiss
            ) {
                Text(text = buttonText)
            }
        },
        title = { Text(text = title) },
        text = { Text(text = message) },
        icon = {
            Icon(
                imageVector = Icons.Outlined.ErrorOutline,
                contentDescription = "Warning"
            )
        },
        modifier = Modifier.testTag(ErrorAlertTestTag)
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ErrorAlertImplPreview() {
    TicTacToeTheme {
        ErrorAlertImpl(
            title = "Error accessing ... ",
            message = "Could not ...",
            buttonText = "OK",
            onDismiss = { }
        )
    }
}
