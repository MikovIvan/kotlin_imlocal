package ru.imlocal.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.imlocal.App
import ru.imlocal.BuildConfig
import ru.imlocal.data.local.AppDatabase.Companion.DATABASE_NAME
import ru.imlocal.data.local.dao.FavActionDao
import ru.imlocal.data.local.dao.FavEventDao
import ru.imlocal.data.local.dao.FavPlaceDao
import ru.imlocal.data.local.entities.FavAction
import ru.imlocal.data.local.entities.FavEvent
import ru.imlocal.data.local.entities.FavPlace

object DbManager {
    val db = Room.databaseBuilder(
        App.applicationContext(),
        AppDatabase::class.java, DATABASE_NAME
    )
        .build()
}

@Database(
    entities = [FavEvent::class, FavAction::class, FavPlace::class],
    version = AppDatabase.DATABASE_VERSION

)

abstract class AppDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "${BuildConfig.APPLICATION_ID}.db"
        const val DATABASE_VERSION = 1
    }

    abstract fun favActionDao(): FavActionDao
    abstract fun favPlaceDao(): FavPlaceDao
    abstract fun favEventDao(): FavEventDao
}