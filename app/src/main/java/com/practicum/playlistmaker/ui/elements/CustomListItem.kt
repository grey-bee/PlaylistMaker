package com.practicum.playlistmaker.ui.elements

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.ui.mock.PreviewData
import com.practicum.playlistmaker.ui.theme.LightGrey
import com.practicum.playlistmaker.ui.theme.PlaylistMakerTheme
import com.practicum.playlistmaker.util.toTimeString


@Composable
fun CustomListItem(track: Track) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(13.dp, 8.dp, 12.dp, 8.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.placeholder),
            contentDescription = "",
            tint = LightGrey
        )
        Column(
            modifier = Modifier
                .padding(8.dp, 14.dp)
        ) {
            Text(
                track.trackName,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSecondary
            )
            Spacer(modifier = Modifier.height(1.dp))
            Text(
                "${track.artistName} • ${track.trackTimeMillis.toTimeString()}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            painter = painterResource(R.drawable.ic_arrow_short_right),
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Preview(name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CustomListItemPreview() {
    PlaylistMakerTheme {
        Surface {
            CustomListItem(PreviewData.track)
        }
    }
}