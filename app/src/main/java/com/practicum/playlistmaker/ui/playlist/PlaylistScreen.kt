package com.practicum.playlistmaker.ui.playlist

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.playlist.ui.details.PlaylistState
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.ui.elements.CustomAlertDialog
import com.practicum.playlistmaker.ui.elements.CustomListItem
import com.practicum.playlistmaker.ui.mock.PreviewData
import com.practicum.playlistmaker.ui.theme.DarkGrey
import com.practicum.playlistmaker.ui.theme.LightGrey
import com.practicum.playlistmaker.ui.theme.PlaylistMakerTheme
import com.practicum.playlistmaker.ui.theme.VeryLightGrey
import com.practicum.playlistmaker.util.toTimeString


@Composable
fun PlaylistScreen(
    onShareClick: () -> Unit,
    onInfoEditingClick: () -> Unit,
    onPlaylistDeleteClick: () -> Unit,
    onTrackClick: (Track) -> Unit,
    playlistState: PlaylistState,
    onPushBack: () -> Unit
) {
    val state = playlistState as? PlaylistState.Content ?: return
    var contentHeight by remember { mutableIntStateOf(0) }
    val density = LocalDensity.current
    var showBottomSheet by remember { mutableStateOf(false) }
    var availableHeight by remember { mutableIntStateOf(0) }
    var showDialog by remember { mutableStateOf(false) }

    PlaylistMakerTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .onGloballyPositioned {
                    availableHeight = it.size.height
                }) {
            BottomSheetScaffold(
                sheetPeekHeight = maxOf(
                    with(density) { (availableHeight - contentHeight).toDp() } - 24.dp,
                    24.dp
                ),
                sheetContainerColor = MaterialTheme.colorScheme.background,
                sheetShape = RoundedCornerShape(16.dp),
                containerColor = VeryLightGrey,
                sheetDragHandle = {
                    Box(
                        contentAlignment = Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp, 8.dp, 0.dp, 0.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .width(50.dp)
                                .height(4.dp)
                                .clip(RoundedCornerShape(44.dp))
                                .background(MaterialTheme.colorScheme.onBackground)
                        )
                    }
                },
                content = {
                    Column(
                        Modifier.onGloballyPositioned { it ->
                            contentHeight = it.size.height
                        }
                    ) {
                        Box() {
                            SubcomposeAsyncImage(
                                model = state.playlist.imagePath,
                                contentDescription = null,
                                modifier = Modifier
                                    .aspectRatio(1F),
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
                            Box(
                                contentAlignment = Center,
                                modifier = Modifier
                                    .padding()
                                    .height(52.dp)
                                    .width(52.dp)
                                    .clickable(enabled = true, onClick = { onPushBack() }),
                            ) {
                                Icon(
                                    painterResource(R.drawable.ic_arrow_left),
                                    tint = MaterialTheme.colorScheme.onSurface,
                                    contentDescription = "",
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            playlistState.playlist.name,
                            modifier = Modifier.padding(16.dp, 0.dp),
                            style = MaterialTheme.typography.titleLarge,
                            color = DarkGrey,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        playlistState.playlist.description?.let {
                            Text(
                                it,
                                modifier = Modifier.padding(16.dp, 0.dp),
                                style = MaterialTheme.typography.displayMedium,
                                color = DarkGrey
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "${
                                pluralStringResource(
                                    R.plurals.tracks_time,
                                    playlistState.playlistTimeSec,
                                    playlistState.playlistTimeSec,
                                )
                            } • ${
                                pluralStringResource(
                                    R.plurals.tracks_count,
                                    playlistState.playlist.trackCount,
                                    playlistState.playlist.trackCount,
                                )
                            }",
                            modifier = Modifier.padding(16.dp, 0.dp),
                            style = MaterialTheme.typography.displayMedium,
                            color = DarkGrey,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(modifier = Modifier.padding(16.dp, 0.dp)) {
                            Icon(
                                painterResource(R.drawable.ic_share),
                                tint = DarkGrey,
                                contentDescription = "",
                                modifier = Modifier.clickable(enabled = true, onClick = {
                                    onShareClick()
                                })
                            )
                            Icon(
                                painterResource(R.drawable.ic_settings2),
                                tint = DarkGrey,
                                contentDescription = "",
                                modifier = Modifier
                                    .padding(16.dp, 0.dp)
                                    .clickable(enabled = true, onClick = {
                                        showBottomSheet = true
                                    }),
                            )
                        }
                    }
                },
                sheetContent = {
                    Spacer(modifier = Modifier.height(12.dp))
                    LazyColumn(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        itemsIndexed(
                            items = state.playlistTracks,
                            key = { index, _ -> index }
                        ) { _, item ->
                            CustomListItem(
                                item.artworkUrl100,
                                item.trackName,
                                "${item.artistName} • ${item.trackTimeMillis.toTimeString()}",
                                { onTrackClick(item) }
                            )
                        }
                    }
                }
            )
        }
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                containerColor = MaterialTheme.colorScheme.background,
                dragHandle = {
                    Box(
                        contentAlignment = Center, modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp, 8.dp, 0.dp, 0.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .width(50.dp)
                                .height(4.dp)
                                .clip(RoundedCornerShape(44.dp))
                                .background(MaterialTheme.colorScheme.onBackground)
                        )
                    }
                }
            ) {
                Spacer(modifier = Modifier.height(12.dp))
                CustomListItem(
                    playlistState.playlist.imagePath,
                    playlistState.playlist.name,
                    pluralStringResource(
                        R.plurals.tracks_count,
                        playlistState.playlist.trackCount,
                        playlistState.playlist.trackCount
                    ),
                    {},
                    false
                )
                Spacer(modifier = Modifier.height(29.dp))
                Text(
                    stringResource(R.string.share),
                    modifier = Modifier
                        .padding(16.dp, 0.dp)
                        .clickable(enabled = true, onClick = { onShareClick() }),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(41.dp))
                Text(
                    stringResource(R.string.information_editing),
                    modifier = Modifier
                        .padding(16.dp, 0.dp)
                        .clickable(enabled = true, onClick = { onInfoEditingClick() }),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(41.dp))
                Text(
                    stringResource(R.string.delete_playlist),
                    modifier = Modifier
                        .padding(16.dp, 0.dp)
                        .clickable(enabled = true, onClick = { showDialog = true }),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(107.dp))
            }
        }
        if (showDialog) {
            CustomAlertDialog(
                R.string.do_you_want_to_delete_playlist,
                null,
                R.string.finish,
                R.string.cancel,
                { onPlaylistDeleteClick() },
                { showDialog = false }
            )
        }


    }
}

@Preview(name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PlaylistScreenPreview() {
    PlaylistScreen(
        {}, {}, {}, {},
        PlaylistState.Content(
            PreviewData.playlist,
            playlistTimeSec = 300,
            playlistTracks = PreviewData.trackList10
        ),
        {}
    )
}