package com.practicum.playlistmaker.ui.medialibrary

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.favorites.ui.FavoritesState
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.ui.elements.CustomListItem
import com.practicum.playlistmaker.ui.elements.ErrorPlaceHolder
import com.practicum.playlistmaker.util.toTimeString

@Composable
fun FavoriteTab(favoritesState: FavoritesState, onTrackClick: (Track) -> Unit) {
    when (favoritesState) {
        is FavoritesState.Empty -> {
            ErrorPlaceHolder(
                R.drawable.ic_nothing_found,
                R.string.your_media_library_is_empty,
                {}, spaceBefore = 46
            )
        }

        is FavoritesState.Content -> {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                itemsIndexed(
                    items = favoritesState.tracks,
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
    }
}