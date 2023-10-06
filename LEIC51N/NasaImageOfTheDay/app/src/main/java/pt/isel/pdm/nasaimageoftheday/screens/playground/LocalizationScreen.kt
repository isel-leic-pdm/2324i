package pt.isel.pdm.nasaimageoftheday.screens.playground

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import pt.isel.pdm.nasaimageoftheday.R
import java.util.Locale

@Composable
fun LocalizationScreen(context : Context)
{
    LocalizationView {
        //DO NOT USE THIS, DEMO PURPOSES ONLY
        val resources = context.getResources()
        val configuration = resources.getConfiguration()

        var localeText  = "pt"
        if(configuration.locale.toLanguageTag() == localeText)
            localeText = "en-US"

        val locale = Locale(localeText)
        Locale.setDefault(locale)
        configuration.locale = locale
        resources.updateConfiguration(configuration, resources.getDisplayMetrics())
        (context as? Activity)?.recreate();
    }
}

@Composable
fun LocalizationView(
    change: ()->Unit
) {
    Column {
        Text(stringResource(id = R.string.language))
        Button(onClick = change) {
            Text(text = stringResource(id = R.string.change_language))
        }
    }
}