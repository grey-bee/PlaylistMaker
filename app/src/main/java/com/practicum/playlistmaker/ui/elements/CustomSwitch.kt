package com.practicum.playlistmaker.ui.elements

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.practicum.playlistmaker.ui.theme.PlaylistMakerTheme
import com.practicum.playlistmaker.ui.theme.LocalTheme

@Composable
fun CustomSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val thumbSize = 18.dp
    val trackWidth = 32.dp
    val trackHeight = 12.dp

    val thumbColor = LocalTheme.current.switchThumb
    val trackColor = LocalTheme.current.switchTrack

    Box(
        modifier = modifier
            .clickable { onCheckedChange(!checked) },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .padding(12.dp, 14.dp)
                .width(trackWidth)
                .height(trackHeight)
                .clip(RoundedCornerShape(14.dp))
                .background(trackColor)
        )
        Box(
            modifier = Modifier
                .offset(x = if (checked) 10.dp else (-10).dp)
                .size(thumbSize)
                .clip(CircleShape)
                .background(thumbColor)
        )
    }
}

@Preview(name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SwitchPreview() {
    PlaylistMakerTheme {
        Surface {
            CustomSwitch(
                checked = true,
                onCheckedChange = {},
            )
        }
    }
}