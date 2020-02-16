package ru.imlocal.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fav_actions")
data class FavAction(
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0,
    val actionId: Int
)