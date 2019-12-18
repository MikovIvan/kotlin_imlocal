package ru.imlocal.models

import com.google.gson.annotations.SerializedName

data class User(
    val id: Int,
    @SerializedName("source_id")
    val source_id: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val middleName: String,
    val userAddress: Address,
    val accessToken: String,
    val source: String,
    val username: String,
    @SerializedName("eventsFavorites")
    val actionsFavoritesList: List<Action>,
    @SerializedName("shopsFavorites")
    val placesFavoritesList: List<Place>,
    @SerializedName("happeningsFavorites")
    val eventsFavoritesList: List<Event>,
    @SerializedName("events")
    val actionsCreatedList: List<Action>,
    @SerializedName("shops")
    val placesCreatedList: List<Place>,
    @SerializedName("happenings")
    val eventsCreatedList: List<Event>,
    val isLogin: Boolean
) {
}