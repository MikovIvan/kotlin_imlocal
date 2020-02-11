package ru.imlocal.models

enum class FavType(val kind: String) {
    PLACE("shop"),
    ACTION("event"),
    EVENT("happening")
}

data class Favorites(
    val actionsFavoritesList: MutableMap<Int, Action>? = mutableMapOf(),
    val placesFavoritesList: MutableMap<Int, Place>? = mutableMapOf(),
    val eventsFavoritesList: MutableMap<Int, Event>? = mutableMapOf()
)