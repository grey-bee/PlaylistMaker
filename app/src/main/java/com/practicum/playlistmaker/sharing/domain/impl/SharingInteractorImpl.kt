package com.practicum.playlistmaker.sharing.domain.impl

import com.practicum.playlistmaker.sharing.domain.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.api.SharingRepository
import com.practicum.playlistmaker.sharing.domain.model.Email
import com.practicum.playlistmaker.sharing.domain.model.Share
import com.practicum.playlistmaker.sharing.domain.model.Url

class SharingInteractorImpl(private val repository: SharingRepository) : SharingInteractor {
    override fun shareApp(): Share {
        val title = repository.getShareApp()
        val text = repository.getCourseLink()
        return Share(text, title)
    }

    override fun writeSupport(): Email {
        val title = repository.getWriteToSupport()
        val address = repository.getMyEmail()
        val subj = repository.getSubjForDevelopers()
        val body = repository.getMessageForDevelopers()
        return Email(address, subj, body, title)
    }

    override fun userAgreement(): Url {
        val url = repository.getPracticumOffer()
        return Url(url)
    }
}