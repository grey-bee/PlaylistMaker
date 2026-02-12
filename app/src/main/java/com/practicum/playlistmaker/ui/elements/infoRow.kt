package com.practicum.playlistmaker.ui.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.R
import com.practicum.playlistmaker.ui.theme.LightGrey

@Composable
fun InfoRow(title: Int, data: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp,8.dp,16.dp,9.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            stringResource(title),
            style = MaterialTheme.typography.bodySmall,
            color = LightGrey
        )
        Text(data,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface)
    }
}