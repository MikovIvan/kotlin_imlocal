package ru.imlocal.models

import com.google.gson.annotations.SerializedName

data class PlacePhoto(
    val id: Int,
    @SerializedName("shopId")
    val shopId: Int,
    @SerializedName("shopPhoto")
    val shopPhoto: String
) {
}