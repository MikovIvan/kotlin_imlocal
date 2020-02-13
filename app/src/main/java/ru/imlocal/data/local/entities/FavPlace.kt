package ru.imlocal.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fav_places")
data class FavPlace(
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0,
    val placeId: Int
)
