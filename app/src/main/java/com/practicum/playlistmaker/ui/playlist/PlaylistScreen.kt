package com.practicum.playlistmaker.ui.playlist

import android.content.res.Configuration
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
fun PlaylistScreen(
//    nameOfScreen: Int,
//    nameOfButton: Int,
//    onPushButton: (Playlist) -> Unit,
//    playlist: Playlist? = null,
//    onPushBack: (() -> Unit)? = null
) {
//    val nameState = rememberTextFieldState(playlist?.name ?: "")
//    val descriptionState = rememberTextFieldState(playlist?.description ?: "")
//    val coverState = remember { mutableStateOf(playlist?.imagePath) }
//    val imageLauncher = rememberLauncherForActivityResult(
//        ActivityResultContracts.PickVisualMedia(),
//        onResult = { item -> coverState.value = item?.toString() }
//    )
//    var showDialog by remember { mutableStateOf(false) }

    PlaylistMakerTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Icon(
                painterResource(R.drawable.ic_arrow_left),
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = ""
            )
            SubcomposeAsyncImage(
                model = R.drawable.placeholder,
                contentDescription = null,
                modifier = Modifier
                    .aspectRatio(1F),
                contentScale = ContentScale.Crop,
                loading = { R.drawable.placeholder },
                error = { R.drawable.placeholder })
        }
    }
}

@Preview(name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PlaylistScreenPreview() {
    PlaylistScreen(
//        nameOfScreen = R.string.new_playlist,
//        nameOfButton = R.string.create,
//        onPushButton = {},
    )
}