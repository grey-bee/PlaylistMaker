package com.practicum.playlistmaker.ui.medialibrary

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.trace
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.playlist.domain.model.Playlist
import com.practicum.playlistmaker.playlist.ui.list.PlaylistsState
import com.practicum.playlistmaker.ui.elements.CustomGridItem
import com.practicum.playlistmaker.ui.elements.ErrorPlaceHolder
import com.practicum.playlistmaker.ui.theme.LightGrey

@Composable
fun PlaylistsTab(
    playlistsState: PlaylistsState,
    onNewPlaylist: () -> Unit,
    onPlaylistClick: (Playlist) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onClick = { onNewPlaylist() }, colors = ButtonColors(
                containerColor = MaterialTheme.colorScheme.onSecondary,
                contentColor = MaterialTheme.colorScheme.background,
                disabledContainerColor = LightGrey,
                disabledContentColor = LightGrey,
            )
        ) {
            Text(
                stringResource(R.string.new_playlist),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        when (playlistsState) {
            is PlaylistsState.Empty -> {
                ErrorPlaceHolder(
                    R.drawable.ic_nothing_found,
                    R.string.didnt_make_any_playlist,
                    {}, spaceBefore = 46
                )
            }

            is PlaylistsState.Content -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(0.dp, 16.dp, 0.dp, 0.dp)
                ) {
                    items(playlistsState.playlists) { item ->
                        CustomGridItem(
                            imageUrl = item.imagePath,
                            title = item.name,
                            trackQty = item.trackCount,
                            onItemClick = { onPlaylistClick(item) }
                        )
                    }
                }
            }
        }
    }
}