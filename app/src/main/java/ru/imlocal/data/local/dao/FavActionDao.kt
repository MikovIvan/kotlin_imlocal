package ru.imlocal.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.imlocal.data.local.entities.FavAction

@Dao
interface FavActionDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavActions(actionsId: List<FavAction>)

    @Insert
    suspend fun insertFavAction(action: FavAction)

    @Query(
        """
            DELETE from fav_actions
            WHERE actionId = :actionId
    """
    )
    suspend fun deleteFavAction(actionId: Int)

    @Query(
        """
            SELECT * from fav_actions
            WHERE actionId = :actionId
    """
    )
    fun findFavAction(actionId: Int): FavAction?

}