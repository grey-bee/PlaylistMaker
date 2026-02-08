package com.practicum.playlistmaker.ui.medialibrary

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.favorites.ui.FavoritesState
import com.practicum.playlistmaker.playlist.domain.model.Playlist
import com.practicum.playlistmaker.playlist.ui.list.PlaylistsState
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.ui.theme.PlaylistMakerTheme
import kotlinx.coroutines.launch

@Composable
fun MediaLibraryScreen(
    favoritesState: FavoritesState,
    playlistsState: PlaylistsState,
    onNewPlaylist: () -> Unit,
    onTrackClick: (Track) -> Unit,
    onPlaylistClick: (Playlist) -> Unit,
) {
    val pagerState = rememberPagerState() { 2 }
    val coroutineScope = rememberCoroutineScope()

    PlaylistMakerTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                stringResource(id = R.string.media_library),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                )
            },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                SecondaryTabRow(
                    selectedTabIndex = pagerState.currentPage, indicator = {
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(
                                pagerState.currentPage,
                                matchContentSize = false
                            )
                        )
                    }
                ) {
                    Tab(
                        selected = pagerState.currentPage == 0,
                        onClick = { coroutineScope.launch { pagerState.animateScrollToPage(0) } },
                        text = { Text(stringResource(R.string.featured_tracks)) }
                    )
                    Tab(
                        selected = pagerState.currentPage == 1,
                        onClick = { coroutineScope.launch { pagerState.animateScrollToPage(1) } },
                        text = { Text(stringResource(R.string.playlists)) }
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                HorizontalPager(
                    pagerState,
                    modifier = Modifier.padding(12.dp, 8.dp, 12.dp, 0.dp)
                ) { page ->
                    when (page) {
                        0 -> {
                            FavoriteTab(favoritesState, onTrackClick)
                        }

                        1 -> {
                            PlaylistsTab(playlistsState, onNewPlaylist, onPlaylistClick)
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
fun MediaLibraryScreenPreview() {
    MediaLibraryScreen(
//        FavoritesState.Content(
//            PreviewData.trackList10
//        ),
        FavoritesState.Empty,
//        PlaylistsState.Content(
//            PreviewData.playlistList10,
//        ),
        PlaylistsState.Empty,
        {}, {}, {}
    )
}