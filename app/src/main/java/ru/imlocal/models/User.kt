package ru.imlocal.models

import com.google.gson.annotations.SerializedName

data class User(
    var id: Int = -1,
    @SerializedName("source_id")
    var source_id: String = "",
    var email: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var middleName: String = "",
    val userAddress: Address? = null,
    var accessToken: String = "",
    var source: String = "",
    var username: String = "",
    @SerializedName("eventsFavorites")
    val actionsFavoritesList: List<Action> = emptyList(),
    @SerializedName("shopsFavorites")
    val placesFavoritesList: List<Place> = emptyList(),
    @SerializedName("happeningsFavorites")
    val eventsFavoritesList: List<Event> = emptyList(),
    @SerializedName("events")
    val actionsCreatedList: List<Action> = emptyList(),
    @SerializedName("shops")
    val placesCreatedList: List<Place> = emptyList(),
    @SerializedName("happenings")
    val eventsCreatedList: List<Event> = emptyList(),
    var isLogin: Boolean = false
)