package pt.isel.pdm.nasaimageoftheday.screens.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import pt.isel.pdm.nasaimageoftheday.R
import pt.isel.pdm.nasaimageoftheday.screens.about.model.SocialInfo
import pt.isel.pdm.nasaimageoftheday.screens.components.NavigationHandlers
import pt.isel.pdm.nasaimageoftheday.screens.components.TopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    backHandler: () -> Unit,
    sendEmailHandler: () -> Unit,
    socialAccountClicked: (SocialInfo) -> Unit,
    socials: List<SocialInfo>
) {
    Scaffold(
        topBar = {
            TopBar(
                title = R.string.about,
                navigationHandlers = NavigationHandlers(
                    onBackHandler = backHandler
                )
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            Author(sendEmailHandler,
                Modifier
                    .align(Alignment.TopCenter)
                    .padding(12.dp))

            Socials(socials, socialAccountClicked,
                Modifier.align(Alignment.Center))
        }
    }
}

@Composable
fun Author(
    sendEmail: () -> Unit,
    modifier: Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.clickable { sendEmail() }
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_author),
            contentDescription = null,
            modifier = Modifier.sizeIn(100.dp, 100.dp, 200.dp, 200.dp)
        )
        Text(text = "Diogo Cardoso", style = MaterialTheme.typography.titleLarge)
        Icon(imageVector = Icons.Default.Email, contentDescription = null)
    }
}


@Composable
private fun Socials(
    socials: List<SocialInfo>,
    openSocial: (SocialInfo) -> Unit,
    modifier: Modifier = Modifier,
) {
    val maxInRow = 2
    Column(modifier = modifier) {

        val nrOfRows = socials.size / maxInRow + 1
        var currIdx = 0
        repeat(nrOfRows) {
            Row {
                var idx = 0
                while (currIdx < socials.size && idx < maxInRow) {
                    val index = currIdx;
                    Social(
                        socialInfo = socials[index],
                        onClick = { openSocial(socials[index]) })
                    currIdx++
                    idx++
                }

            }
        }

    }
}

@Composable
fun Social(
    socialInfo: SocialInfo,
    onClick: () -> Unit
) {
    Image(
        painter = painterResource(id = socialInfo.imageId),
        contentDescription = null,
        modifier = Modifier
            .sizeIn(maxWidth = 64.dp)
            .padding(6.dp)
            .clickable { onClick() }
    )
}
