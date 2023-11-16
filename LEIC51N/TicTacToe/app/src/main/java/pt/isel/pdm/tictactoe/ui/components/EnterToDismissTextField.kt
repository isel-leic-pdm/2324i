package pt.isel.pdm.tictactoe.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import pt.isel.pdm.tictactoe.R

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun EnterToDismissTextField(
    userNameSet: (String) -> Unit,
    autofocus: Boolean = false,
    modifierArg: Modifier = Modifier
) {
    var userName by rememberSaveable { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var modifier = modifierArg

    if (autofocus)
        modifier = modifier.focusRequester(focusRequester)

    TextField(
        value = userName,
        placeholder = { Text(stringResource(id = R.string.user_name)) },
        onValueChange = { userName = it },
        singleLine = true,
        modifier = modifier,

        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
                userNameSet(userName)
            }),
    )
    
    if(autofocus)
        LaunchedEffect(Unit ){
            focusRequester.requestFocus()
        }


}