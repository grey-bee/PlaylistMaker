package com.practicum.playlistmaker.sharing.data

import android.content.Context
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.sharing.domain.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.model.Email
import com.practicum.playlistmaker.sharing.domain.model.Share
import com.practicum.playlistmaker.sharing.domain.model.Url

class SharingInteractorImpl(private val context: Context) : SharingInteractor {
    override fun shareApp(): Share {
        val title = context.getString(R.string.share_app)
        val text = context.getString(R.string.course_link)
        return Share(text, title)
    }

    override fun writeSupport(): Email {
        val title = context.getString(R.string.write_to_support)
        val address = context.getString(R.string.my_email)
        val subj = context.getString(R.string.subj_for_developers)
        val body = context.getString(R.string.message_for_developers)
        return Email(address, subj, body, title)
    }

    override fun userAgreement(): Url {
        val url = context.getString(R.string.practicum_offer)
        return Url(url)
    }
}