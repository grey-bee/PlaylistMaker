package com.practicum.playlistmaker.ui.player

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.player.ui.PlayerState
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.ui.elements.InfoRow
import com.practicum.playlistmaker.ui.mock.PreviewData
import com.practicum.playlistmaker.ui.theme.LightGrey
import com.practicum.playlistmaker.ui.theme.PlaylistMakerTheme
import com.practicum.playlistmaker.util.toTimeString
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.practicum.playlistmaker.ui.elements.CustomListItem

@Composable
fun PlayerScreen(
    track: Track,
    playerState: PlayerState,
    isFavorite: Boolean,
    onPushPlaybutton: () -> Unit,
    onFavoriteClick: (Track) -> Unit,
    onAddToPlaylist: (Track) -> Unit,
    onPushBack: () -> Unit,
) {
    val modalState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    PlaylistMakerTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    modifier = Modifier.clickable(onClick = { onPushBack() }, enabled = true),
                    title = {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Icon(
                                painterResource(R.drawable.ic_arrow_left),
                                tint = MaterialTheme.colorScheme.onSurface,
                                contentDescription = ""
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
                    .verticalScroll(rememberScrollState())
            ) {
                SubcomposeAsyncImage(
                    model = track.artworkUrl512,
                    contentDescription = null,
                    modifier = Modifier
                        .aspectRatio(1F)
                        .padding(24.dp, 26.dp, 24.dp, 0.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop,
                    loading = {
                        Icon(
                            painterResource(R.drawable.placeholder),
                            modifier = Modifier,
                            tint = LightGrey,
                            contentDescription = "",
                        )
                    },
                    error = {
                        Icon(
                            painterResource(R.drawable.placeholder),
                            modifier = Modifier,
                            tint = LightGrey,
                            contentDescription = "",
                        )
                    })
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    track.trackName,
                    modifier = Modifier.padding(24.dp, 0.dp),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    track.artistName,
                    modifier = Modifier.padding(24.dp, 0.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(30.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp, 0.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painterResource(R.drawable.button_add_to_playlist),
                        modifier = Modifier.clickable(
                            enabled = true,
                            onClick = { showBottomSheet = true }
                        ),
                        contentDescription = "",
                        tint = Color.Unspecified
                    )
                    Icon(
                        painterResource(
                            when (playerState) {
                                is PlayerState.Playing -> R.drawable.button_pause
                                else -> R.drawable.button_play
                            }
                        ),
                        modifier = Modifier.clickable(
                            enabled = playerState.isPlayButtonEnabled,
                            onClick = { onPushPlaybutton() }
                        ),
                        contentDescription = "",
                        tint = Color.Unspecified
                    )
                    Icon(
                        painterResource(
                            if (isFavorite) R.drawable.button_like
                            else R.drawable.button_unlike
                        ),
                        modifier = Modifier.clickable(
                            enabled = true,
                            onClick = { onFavoriteClick(track) }),
                        contentDescription = "",
                        tint = Color.Unspecified
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    playerState.progress,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(30.dp))
                InfoRow(R.string.duration, track.trackTimeMillis.toTimeString())
                if (track.collectionName.isNotEmpty()) {
                    InfoRow(R.string.album, track.collectionName)
                }
                if (track.releaseYear.isEmpty()) {
                    InfoRow(R.string.year, track.releaseYear)
                }
                InfoRow(R.string.genre, track.primaryGenreName)
                InfoRow(R.string.country, track.country)

            }
        }
        ModalBottomSheet(
            onDismissRequest = {},
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    stringResource(R.string.add_to_playlist),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { }, colors = ButtonColors(
                        containerColor = MaterialTheme.colorScheme.onSecondary,
                        contentColor = MaterialTheme.colorScheme.background,
                        disabledContainerColor = LightGrey,
                        disabledContentColor = LightGrey,
                    ),
                    modifier = Modifier.padding(0.dp, 24.dp, 0.dp, 0.dp)
                ) {
                    Text(
                        stringResource(R.string.new_playlist),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
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
                            onClick = {  }, colors = ButtonColors(
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
    }
}

@Preview(name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PlayerScreenPreview() {
    PlayerScreen(
        PreviewData.track,
        PlayerState.Playing(
            progress = "00:30"
        ), true,
        {},
        {},
        {},
        {}
    )
}