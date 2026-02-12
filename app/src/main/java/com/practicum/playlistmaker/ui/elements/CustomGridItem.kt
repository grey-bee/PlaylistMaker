package com.practicum.playlistmaker.ui.elements

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.ui.mock.PreviewData
import com.practicum.playlistmaker.ui.theme.LightGrey
import com.practicum.playlistmaker.ui.theme.PlaylistMakerTheme


@Composable
fun CustomGridItem(
    imageUrl: Any?,
    title: String,
    trackQty: Int,
    onItemClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp, 8.dp)
            .clickable { onItemClick() }
    ) {
        SubcomposeAsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier
                .aspectRatio(1F)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop,
            loading = {
                Icon(
                    painterResource(R.drawable.placeholder),
                    tint = LightGrey,
                    contentDescription = ""
                )
            },
            error = {
                Icon(
                    painterResource(R.drawable.placeholder),
                    tint = LightGrey,
                    contentDescription = ""
                )
            }
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            title,
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onSecondary
        )
        Spacer(modifier = Modifier.height(1.dp))
        Text(
            pluralStringResource(R.plurals.tracks_count, trackQty, trackQty),
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onSecondary
        )
    }
}


@Preview(name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CustomGridItemPreview() {
    val playlist = PreviewData.playlist
    PlaylistMakerTheme {
        Surface {
            CustomGridItem(
                playlist.imagePath,
                playlist.name,
                playlist.trackCount,
                {}
            )
        }
    }
}