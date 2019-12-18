package ru.imlocal.models

import com.google.gson.annotations.SerializedName

data class Action(
    var id: Int,
    val active: Int,
    @SerializedName("isEventTop")
    val isActionTop: Int,
    @SerializedName("eventOwnerId")
    val actionOwnerId: Int,
    @SerializedName("eventTypeId")
    val actionTypeId: Int,
    val title: String,
    val fullDesc: String,
    val begin: String,
    val end: String,
    val creatorId: Int,
    @SerializedName("eventPhotos")
    val actionPhotos: List<ActionPhoto>,
    @SerializedName("shop")
    val place: Place
) {
}