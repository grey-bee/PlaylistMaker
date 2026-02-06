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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.search.ui.SearchScreenState
import com.practicum.playlistmaker.ui.elements.CustomListItem
import com.practicum.playlistmaker.ui.elements.CustomSearchField
import com.practicum.playlistmaker.ui.elements.ErrorPlaceHolder
import com.practicum.playlistmaker.ui.mock.PreviewData
import com.practicum.playlistmaker.ui.theme.LightGrey
import com.practicum.playlistmaker.ui.theme.MainBlue
import com.practicum.playlistmaker.ui.theme.PlaylistMakerTheme
import com.practicum.playlistmaker.util.toTimeString


@Composable
fun SearchScreen(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    state: SearchScreenState,
    onTrackClick: (Track) -> Unit,
    onCleanHistory: () -> Unit,
    onRefreshConnection: () -> Unit
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
                when (state) {
                    is SearchScreenState.History -> {
                        if (state.tracks.isNotEmpty()) {
                            LazyColumn(
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                item {
                                    Text(
                                        stringResource(id = R.string.you_search),
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.padding(0.dp, 42.dp, 0.dp, 12.dp)
                                    )
                                }
                                itemsIndexed(
                                    items = state.tracks,
                                    key = { index, _ -> index }
                                ) { _, item ->
                                    CustomListItem(
                                        item.artworkUrl100,
                                        item.trackName,
                                        "${item.artistName} • ${item.trackTimeMillis.toTimeString()}",
                                        { onTrackClick(item) }
                                    )
                                }
                                item {
                                    Button(
                                        onClick = { onCleanHistory() }, colors = ButtonColors(
                                            containerColor = MaterialTheme.colorScheme.onSecondary,
                                            contentColor = MaterialTheme.colorScheme.background,
                                            disabledContainerColor = LightGrey,
                                            disabledContentColor = LightGrey,
                                        ),
                                        modifier = Modifier.padding(0.dp, 24.dp, 0.dp, 0.dp)
                                    ) {
                                        Text(
                                            stringResource(R.string.history_clear),
                                            style = MaterialTheme.typography.bodyMedium,
                                        )
                                    }

                                }
                            }
                        }
                    }

                    is SearchScreenState.Empty -> {
                        ErrorPlaceHolder(
                            R.drawable.ic_nothing_found,
                            R.string.nothing_found,
                            {},
                        )
                    }

                    is SearchScreenState.Content -> {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            itemsIndexed(
                                items = state.tracks,
                                key = { index, _ -> index }) { _, item ->
                                CustomListItem(
                                    item.artworkUrl100,
                                    item.trackName,
                                    "${item.artistName} • ${item.trackTimeMillis.toTimeString()}",
                                    { onTrackClick(item) }
                                )
                            }
                        }
                    }

                    is SearchScreenState.Error -> {
                        ErrorPlaceHolder(
                            R.drawable.ic_no_connection,
                            R.string.no_connection,
                            { onRefreshConnection() },
                            R.string.refresh
                        )
                    }

                    is SearchScreenState.Loading -> {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            CircularProgressIndicator(color = MainBlue)
                        }
                    }

                }

            }
        }
    }
}

@Preview(name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SearchPreview() {
//    SearchScreen("text", {}, SearchScreenState.Empty, {})
    SearchScreen(
        "text",
        {},
        SearchScreenState.History(PreviewData.trackList5),
        {},
        {},
        {}
    )
}