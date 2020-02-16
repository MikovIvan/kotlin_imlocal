package ru.imlocal.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fav_events")
data class FavEvent(
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0,
    val eventId: Int
)