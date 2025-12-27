package com.practicum.playlistmaker.sharing.domain

import android.content.Intent

interface SharingInteractor {
    fun shareApp(): Intent
    fun writeSupport(): Intent
    fun userAgreement(): Intent
}