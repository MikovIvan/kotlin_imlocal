package ru.imlocal.models

import com.google.gson.annotations.SerializedName

data class EventPhoto(
    val id: Int,
    @SerializedName("happeningId")
    val eventId: Int,
    @SerializedName("happeningPhoto")
    val eventPhoto: String
) {
}