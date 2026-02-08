package com.practicum.playlistmaker.ui.elements

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.ui.theme.LightGrey
import com.practicum.playlistmaker.ui.theme.PlaylistMakerTheme

@Composable
fun ErrorPlaceHolder(
    icon: Int,
    description: Int,
    onButtonClick: () -> Unit,
    buttonTitle: Int? = null,
    spaceBefore: Int = 102
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(spaceBefore.dp))
        Image(
            painterResource(icon),
            contentDescription = "",
            modifier = Modifier
                .height(120.dp)
                .width(120.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            stringResource(description),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        buttonTitle?.let { title ->
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { onButtonClick() }, colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.onSecondary,
                    contentColor = MaterialTheme.colorScheme.background,
                    disabledContainerColor = LightGrey,
                    disabledContentColor = LightGrey,
                )
            ) {
                Text(
                    stringResource(title),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

        }
    }
}

@Preview(name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ErrorPlaceHolderPreview() {
    PlaylistMakerTheme {
        Surface {
            ErrorPlaceHolder(
                R.drawable.ic_nothing_found,
                R.string.no_connection,
                {},
                R.string.refresh
            )
        }
    }
}