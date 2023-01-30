package com.example.tvshow.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.tvshow.R
import com.example.tvshow.model.Show

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    viewModel: MainViewModel,
    navController: NavHostController,
    id: String
) {
    val tVShow = viewModel.searchResponse.collectAsState().value.data?.find {
        it.show.id == id.toInt()
    }?.show
    if (tVShow != null)
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = tVShow.name) },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "")
                        }
                    },
                )
            }
        ) {
            Surface(modifier = Modifier.fillMaxSize()) {
                TVDetailContent(modifier = Modifier.padding(it), tVShow)
            }
        }
}

@Composable
private fun TVDetailContent(modifier: Modifier = Modifier, show: Show) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp)
            .verticalScroll(rememberScrollState())
    ) {
        OverView(show)
        Divider(modifier = Modifier.padding(vertical = 12.dp))
        Summary(show)
    }
}

@Composable
private fun OverView(show: Show) {
    Row(modifier = Modifier.fillMaxWidth()) {
        AsyncImage(
            model =
            if (show.image != null) show.image.original
            else R.drawable.placeholder_image,
            contentDescription = "",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.weight(2f)
        )
        Column(
            modifier = Modifier
                .weight(3f)
                .padding(start = 12.dp)
        ) {
            OverviewItem("Type", show.type)
            if (show.genres.isNotEmpty())
                OverviewItem("Genre", show.genres.toString().drop(1).dropLast(1))
            OverviewItem("Rating", show.rating.average.toString())
            OverviewItem("Premiered", show.premiered)
            OverviewItem("Ended", show.ended)
            OverviewItem("Status", show.status)
            OverviewItem("Average runtime", show.averageRuntime.toString())
            OverviewItem("Language", show.language)
            OverviewItem("Network", show.network?.name)
            OverviewItem("Official site", show.officialSite, true)
        }
    }
}

@Composable
private fun OverviewItem(string: String, string2: String, isLink: Boolean = false) {
    if (string2 != null)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "$string: ")
            if (!isLink)
                Text(
                    text = string2,
                    color = Color.Gray,
                    textAlign = TextAlign.End,
                )
            else {
                val uriHandler = LocalUriHandler.current
                ClickableText(
                    text = AnnotatedString(string2),
                    onClick = { uriHandler.openUri(string2) },
                    style = TextStyle.Default.copy(
                        MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.End
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
}

@Composable
private fun Summary(show: Show) {
    if (show.summary != null)
        Text(
            text = "Summary: \n" + show.summary
                .replace("<p>", "\n")
                .replace("</p>", "")
                .replace("<b>", "")
                .replace("</b>", "")
        )
}