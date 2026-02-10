package com.practicum.playlistmaker.ui.player

import android.content.res.Configuration
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
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
import com.practicum.playlistmaker.ui.playlist.IconNewPlaylist
import com.practicum.playlistmaker.ui.theme.LightGrey
import com.practicum.playlistmaker.ui.theme.PlaylistMakerTheme

@Composable
fun PlayerScreen(
//        onShareClick: () -> Unit,
//        onSupportClick: () -> Unit,
//        onAgreementClick: () -> Unit,
//        isDarkTheme: Boolean,
//        onThemeChange: (Boolean) -> Unit
) {
    PlaylistMakerTheme {
        Scaffold(
            topBar = {
                TopAppBar(
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
            ) {
                SubcomposeAsyncImage(
                    model = {},
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
                Text("Yesterday (Remastered 2009)")
                Text("The Beatles ")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        painterResource(R.drawable.placeholder),
                        modifier = Modifier,
                        tint = LightGrey,
                        contentDescription = "",
                    )
                    Icon(
                        painterResource(R.drawable.placeholder),
                        modifier = Modifier,
                        tint = LightGrey,
                        contentDescription = "",
                    )
                    Icon(
                        painterResource(R.drawable.placeholder),
                        modifier = Modifier,
                        tint = LightGrey,
                        contentDescription = "",
                    )
                }
                Text("0:30")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "0:30",
                        style = MaterialTheme.typography.bodySmall,
                        color = LightGrey
                    )
                    Text("0:30",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("0:30")
                    Text("0:30")
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("0:30")
                    Text("0:30")
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("0:30")
                    Text("0:30")
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("0:30")
                    Text("0:30")
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
    )
}
