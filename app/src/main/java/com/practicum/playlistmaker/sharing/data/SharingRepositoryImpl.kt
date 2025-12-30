package com.practicum.playlistmaker.sharing.data

import android.content.Context
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.sharing.domain.api.SharingRepository


class SharingRepositoryImpl(private val context: Context) : SharingRepository {
    override fun getShareApp(): String =
        context.getString(R.string.share_app)

    override fun getCourseLink(): String =
        context.getString(R.string.course_link)

    override fun getWriteToSupport(): String =
        context.getString(R.string.write_to_support)

    override fun getMyEmail(): String =
        context.getString(R.string.my_email)

    override fun getSubjForDevelopers(): String =
        context.getString(R.string.subj_for_developers)

    override fun getMessageForDevelopers(): String =
        context.getString(R.string.message_for_developers)

    override fun getPracticumOffer(): String =
        context.getString(R.string.practicum_offer)

}