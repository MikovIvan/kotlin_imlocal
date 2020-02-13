package ru.imlocal.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.imlocal.data.local.entities.FavEvent

@Dao
interface FavEventDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavEvents(eventsId: List<FavEvent>)

    @Insert
    suspend fun insertFavEvent(event: FavEvent)

    @Query(
        """
            DELETE from fav_events
            WHERE eventId = :eventId
    """
    )
    suspend fun deleteFavEvent(eventId: Int)

    @Query(
        """
            SELECT * from fav_events
            WHERE eventId = :eventId
    """
    )
    fun findFavEvent(eventId: Int): FavEvent?

}