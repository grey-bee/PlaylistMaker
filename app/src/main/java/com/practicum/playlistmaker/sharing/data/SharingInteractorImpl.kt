package com.practicum.playlistmaker.sharing.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.sharing.domain.SharingInteractor

class SharingInteractorImpl(private val context: Context) : SharingInteractor {
    override fun shareApp(): Intent {
        val intent = Intent(Intent.ACTION_SEND)
        val chooserTitle = context.getString(R.string.share_app)
        intent.type = "text/plain"
        intent.putExtra(
            Intent.EXTRA_TEXT,
            context.getString(R.string.course_link)
        )
        return Intent.createChooser(intent, chooserTitle)
    }

    override fun writeSupport(): Intent {
        val intent = Intent(Intent.ACTION_SENDTO)
        val chooserTitle = context.getString(R.string.write_to_support)

        val mailto = "mailto:${context.getString(R.string.my_email)}" +
                "?subject=${Uri.encode(context.getString(R.string.subj_for_developers))}" +
                "&body=${Uri.encode(context.getString(R.string.message_for_developers))}"

        intent.data = mailto.toUri()
        return Intent.createChooser(intent, chooserTitle)
    }

    override fun userAgreement(): Intent {
        val url = context.getString(R.string.practicum_offer).toUri()
        return Intent(Intent.ACTION_VIEW, url)
    }
}