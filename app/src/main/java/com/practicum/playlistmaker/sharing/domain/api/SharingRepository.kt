package com.practicum.playlistmaker.sharing.domain.api

interface SharingRepository {
    fun getShareApp(): String
    fun getCourseLink(): String
    fun getWriteToSupport(): String
    fun getMyEmail(): String
    fun getSubjForDevelopers(): String
    fun getMessageForDevelopers(): String
    fun getPracticumOffer(): String
}