package ru.imlocal.models

import com.google.gson.annotations.SerializedName

data class Event(
    val id: Int,
    val shopId: Int,
    val creatorId: Int,
    val title: String,
    val description: String,
    val address: String,
    val price: String,
    val begin: String,
    val end: String,
    @SerializedName("happeningTypeId")
    val eventTypeId: Int,
    @SerializedName("happeningPhotos")
    val eventPhotos: List<EventPhoto>
) {
}