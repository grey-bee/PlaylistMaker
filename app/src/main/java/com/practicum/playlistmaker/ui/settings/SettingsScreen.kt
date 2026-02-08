package com.practicum.playlistmaker.ui.settings

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.ui.theme.PlaylistMakerTheme

@Composable
fun SettingsScreen(
    onShareClick: () -> Unit,
    onSupportClick: () -> Unit,
    onAgreementClick: () -> Unit,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    val items = listOf(
        SettingItem(
            R.string.dark_theme, SettingType.Switch(isDarkTheme, onThemeChange)
        ),
        SettingItem(
            R.string.share,
            SettingType.ItemIcon(R.drawable.ic_share)
        ) { onShareClick() },
        SettingItem(
            R.string.write_to_support,
            SettingType.ItemIcon(R.drawable.ic_support)
        ) { onSupportClick() },
        SettingItem(
            R.string.user_agreement,
            SettingType.ItemIcon(R.drawable.ic_arrow_short_right)
        ) { onAgreementClick() },
    )
    PlaylistMakerTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                stringResource(id = R.string.settings),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
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
                Spacer(modifier = Modifier.height(24.dp))
                items.forEach { item -> SettingRow(item) }
            }
        }
    }
}

@Preview(name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SettingsPreview() {
            SettingsScreen(
                {},
                {},
                {},
                true,
                {}
            )
}