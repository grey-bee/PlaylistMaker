package com.practicum.playlistmaker.ui.search

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.ui.elements.CustomListItem
import com.practicum.playlistmaker.ui.elements.CustomSearchField
import com.practicum.playlistmaker.ui.mock.PreviewData
import com.practicum.playlistmaker.ui.theme.PlaylistMakerTheme
import com.practicum.playlistmaker.util.toTimeString


@Composable
fun SearchScreen(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    tracks: List<Track>,
    onTrackClick: (Track) -> Unit
) {

    PlaylistMakerTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    println("Клик по всей шапке!")
                                },
                        ) {
                            Text(
                                stringResource(id = R.string.search),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                CustomSearchField(
                    value = searchText,
                    onValueChange = onSearchTextChange
                )
                tracks.forEach {
                    CustomListItem(
                        it.artworkUrl100,
                        it.trackName,
                        "${it.artistName} • ${it.trackTimeMillis.toTimeString()}",
                        { onTrackClick(it) }
                    )
                }
            }
        }
    }
}

@Preview(name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SearchPreview() {
    SearchScreen("text", {}, PreviewData.trackList, {})
}