package ru.imlocal.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.imlocal.data.local.entities.FavPlace

@Dao
interface FavPlaceDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavPlaces(placesId: List<FavPlace>)

    @Insert
    suspend fun insertFavPlace(place: FavPlace)

    @Query(
        """
            DELETE from fav_places
            WHERE placeId = :placeId
    """
    )
    suspend fun deleteFavPlace(placeId: Int)

    @Query(
        """
            SELECT * from fav_places
            WHERE placeId = :placeId
    """
    )
    fun findFavPlace(placeId: Int): FavPlace?

}