package com.practicum.playlistmaker.sharing.domain

import com.practicum.playlistmaker.sharing.domain.model.Share
import com.practicum.playlistmaker.sharing.domain.model.Email
import com.practicum.playlistmaker.sharing.domain.model.Url

interface SharingInteractor {
    fun shareApp(): Share
    fun writeSupport(): Email
    fun userAgreement(): Url
}