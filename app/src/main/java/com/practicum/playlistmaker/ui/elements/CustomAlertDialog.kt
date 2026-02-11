package com.practicum.playlistmaker.ui.elements

import android.content.res.Configuration
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.ui.theme.DarkGrey
import com.practicum.playlistmaker.ui.theme.MainBlue
import com.practicum.playlistmaker.ui.theme.White

@Composable
fun CustomAlertDialog(
    title: Int,
    text: Int?,
    successButtonText: Int,
    cancelButtonText: Int,
    onPushSuccess: (() -> Unit)?,
    onPushCancel: () -> Unit,

    ) {
    AlertDialog(
        onDismissRequest = { onPushCancel() },
        containerColor = White,
        shape = RoundedCornerShape(4.dp),
        title = {
            Text(
                stringResource(title),
                color = DarkGrey,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            )
        },
        text = {
            if (text != null) {
                Text(
                    stringResource(text),
                    color = DarkGrey,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp
                )
            } else {
                ""
            }
        },
        confirmButton = {
            TextButton(onClick = { onPushSuccess?.invoke() }) {
                Text(
                    stringResource(successButtonText),
                    color = MainBlue,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                )
            }
        },
        dismissButton = {
            TextButton(onClick = { onPushCancel() }) {
                Text(
                    stringResource(cancelButtonText),
                    color = MainBlue,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                )
            }
        }
    )
}

@Preview(name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CustomAlertDialogPreview() {
    CustomAlertDialog(
        R.string.finish_playlist_creating,
        R.string.all_unsaved_data_will_be_loose,
        R.string.finish,
        R.string.cancel,
        {},
        {}
    )
}