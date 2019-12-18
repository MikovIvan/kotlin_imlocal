package ru.imlocal.models

import com.google.gson.annotations.SerializedName

data class ActionPhoto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("eventId")
    val actionId: Int,
    @SerializedName("eventPhoto")
    val actionPhoto: String
) {
}