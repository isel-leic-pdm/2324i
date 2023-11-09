package pt.isel.pdm.nasaimageoftheday.screens.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pt.isel.pdm.nasaimageoftheday.R
import pt.isel.pdm.nasaimageoftheday.model.NasaImage
import pt.isel.pdm.nasaimageoftheday.screens.components.NasaImageTextView
import pt.isel.pdm.nasaimageoftheday.screens.components.NavigationHandlers
import pt.isel.pdm.nasaimageoftheday.screens.components.TopBar
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    onBack: () -> Unit,
    onLoadMore: () -> Unit,
    onImageClicked: (NasaImage) -> Unit,
    startDate: LocalDate,
    endDate: LocalDate,
    images: List<NasaImage>
) {
    Scaffold(
        topBar = { TopBar(navigationHandlers = NavigationHandlers(onBackHandler = onBack)) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
/*
            EagerComponent(
                images = images,
                modifier = Modifier.weight(1f)
            )
*/
            LazyComponent(
                images = images,
                modifier = Modifier.weight(1f),
                onImageClicked = onImageClicked
            )

            Column {
                Text(text = "$startDate - $endDate")
                Button(onClick = onLoadMore) {
                    Text(stringResource(id = R.string.load_images))
                }
            }
        }
    }
}

@Composable
fun LazyComponent(
    images: List<NasaImage>,
    modifier: Modifier,
    onImageClicked: (NasaImage) -> Unit
) {

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(36.dp)
    )
    {
        items(images)
        {
            NasaImageTextView(
                nasaImage = it,
                modifier.clickable(true, onClick = {
                    onImageClicked(it)
                })
            )
        }
    }
}

@Composable
fun EagerComponent(images: List<NasaImage>, modifier: Modifier) {
    Column(
        modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {

        for (image in images) {
            NasaImageTextView(nasaImage = image, modifier = Modifier.padding(12.dp))
        }

    }
}
