package com.practicum.playlistmaker.ui.settings

import android.content.res.Configuration
import com.practicum.playlistmaker.R
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.practicum.playlistmaker.ui.elements.CustomSwitch
import com.practicum.playlistmaker.ui.theme.PlaylistMakerTheme

@Composable
fun SettingRow(item: SettingItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 0.dp, 0.dp, 0.dp)
            .height(61.dp)
            .clickable { item.onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(item.nameItem),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        when (val type = item.type) {
            is SettingType.Switch -> {
                CustomSwitch(type.checked, type.onCheckedChange)
            }

            is SettingType.ItemIcon -> {
                Icon(
                    painter = painterResource(id = type.iconRes),
                    contentDescription = null,
                    modifier = Modifier.padding(12.dp),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}


@Preview(name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SettingRowPreview() {
    PlaylistMakerTheme {
        Surface {
            SettingRow(
                SettingItem(
                    nameItem = R.string.settings,
                    type = SettingType.Switch(true, {})
//                    type = SettingType.ItemIcon(R.drawable.ic_share),
//                    onClick = {}
                ),
            )
        }
    }
}