package ru.imlocal.models

import com.google.gson.annotations.SerializedName

data class ShopPhoto(
    val id: Int,
    @SerializedName("shopId")
    val shopId: Int,
    @SerializedName("shopPhoto")
    val shopPhoto: String
) {
}