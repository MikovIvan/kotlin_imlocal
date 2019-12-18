package ru.imlocal.models

import com.google.gson.annotations.SerializedName

data class Place(
    val shopId: Int,
    val creatorId: String,
    val shopActive: Byte,
    val shopShortName: String,
    val shopTypeId: Byte,
    val shopPhone: String?,
    val shopWeb: String,
    val shopAddressId: Int,
    val shopCostMin: String,
    val shopCostMax: String,
    val shopWorkTime: String,
    val shopStatusId: Int,
    val shopShortDescription: String,
    val shopFullDescription: String,
    val shopRating: String,
    val shopPhotos: List<ShopPhoto> = listOf(),
    @SerializedName("events")
    val shopActionList: List<Action> = listOf(),
    @SerializedName("happenings")
    val shopEventList: List<Event> = listOf(),
    val shopAddress: Address,
    val shopAvgRating: Double,
    val shopLinkPdf: String
) {

}