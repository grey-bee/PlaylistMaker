package com.practicum.playlistmaker.ui.playlist

import android.content.res.Configuration
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.playlist.domain.model.Playlist
import com.practicum.playlistmaker.ui.elements.CustomAlertDialog
import com.practicum.playlistmaker.ui.theme.LightGrey
import com.practicum.playlistmaker.ui.theme.MainBlue
import com.practicum.playlistmaker.ui.theme.PlaylistMakerTheme
import com.practicum.playlistmaker.ui.theme.White

@Composable
fun PlaylistEditScreen(
    nameOfScreen: Int,
    nameOfButton: Int,
    onPushButton: (Playlist) -> Unit,
    playlist: Playlist? = null,
    onPushBack: (() -> Unit)? = null
) {
    val nameState = rememberTextFieldState(playlist?.name ?: "")
    val descriptionState = rememberTextFieldState(playlist?.description ?: "")
    val coverState = remember { mutableStateOf(playlist?.imagePath) }
    val imageLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia(),
        onResult = { item -> coverState.value = item?.toString() }
    )
    var showDialog by remember { mutableStateOf(false) }

    PlaylistMakerTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    modifier = Modifier.clickable {
                        if (playlist == null && nameState.text.isNotEmpty()) showDialog = true
                        else onPushBack?.invoke()
                    },
                    title = {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (onPushBack != null) {
                                Icon(
                                    painterResource(R.drawable.ic_arrow_left),
                                    tint = MaterialTheme.colorScheme.onSurface,
                                    contentDescription = ""
                                )
                                Spacer(modifier = Modifier.width(24.dp))
                            }
                            Text(
                                stringResource(nameOfScreen),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                )
            }, bottomBar = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(17.dp, 0.dp, 17.dp, 32.dp),
                        shape = RoundedCornerShape(8.dp),
                        enabled = !nameState.text.isEmpty(),
                        colors = ButtonColors(
                            containerColor = MainBlue,
                            contentColor = White,
                            disabledContainerColor = LightGrey,
                            disabledContentColor = White
                        ),
                        content = { Text(stringResource(nameOfButton)) },
                        onClick = {
                            onPushButton(
                                (if (playlist != null) {
                                    playlist.copy(
                                        name = nameState.text.toString(),
                                        description = descriptionState.text.toString(),
                                        imagePath = coverState.value
                                    )
                                } else {
                                    Playlist(
                                        name = nameState.text.toString(),
                                        description = descriptionState.text.toString(),
                                        imagePath = coverState.value,
                                        id = 0,
                                        trackIds = emptyList(),
                                        trackCount = 0
                                    )

                                })
                            )
                        })
                }
            }) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                SubcomposeAsyncImage(
                    model = coverState.value,
                    contentDescription = null,
                    modifier = Modifier
                        .aspectRatio(1F)
                        .padding(24.dp, 26.dp, 24.dp, 0.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            imageLauncher.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )
                        }
                        .drawBehind {
                            drawRoundRect(
                                cornerRadius = CornerRadius(8.dp.toPx()), style = Stroke(
                                    width = 1.dp.toPx(), pathEffect = PathEffect.dashPathEffect(
                                        intervals = floatArrayOf(
                                            34.dp.toPx(), 34.dp.toPx()
                                        )
                                    )
                                ), color = LightGrey
                            )
                        },
                    contentScale = ContentScale.Crop,
                    loading = { IconNewPlaylist() },
                    error = { IconNewPlaylist() })
                OutlinedTextField(
                    state = nameState,
                    label = {
                        Text(
                            stringResource(R.string.title),
                            style = MaterialTheme.typography.labelMedium,
                        )
                    },
                    textStyle = (MaterialTheme.typography.labelMedium),
                    shape = RoundedCornerShape(4.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedLabelColor = MainBlue,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        focusedBorderColor = MainBlue
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 32.dp, 16.dp, 0.dp)
                )
                OutlinedTextField(
                    state = descriptionState,
                    label = {
                        Text(
                            stringResource(R.string.description),
                            style = MaterialTheme.typography.labelMedium,
                        )
                    },
                    textStyle = (MaterialTheme.typography.labelMedium),
                    shape = RoundedCornerShape(4.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedLabelColor = MainBlue,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        focusedBorderColor = MainBlue
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 16.dp, 16.dp, 0.dp)
                )
            }
        }
    }
    if (showDialog) {
        CustomAlertDialog(
            R.string.finish_playlist_creating,
            R.string.all_unsaved_data_will_be_loose,
            R.string.finish,
            R.string.cancel,
            onPushBack,
            { showDialog = false }
        )
    }
}

@Composable
fun IconNewPlaylist() {
    Box(contentAlignment = Alignment.Center) {
        Icon(
            painterResource(R.drawable.ic_new_playlist),
            modifier = Modifier.size(100.dp),
            tint = LightGrey,
            contentDescription = "",
        )
    }
}


@Preview(name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PlaylistEditScreenPreview() {
    PlaylistEditScreen(
        nameOfScreen = R.string.new_playlist,
        nameOfButton = R.string.create,
        onPushButton = {},
    )
}