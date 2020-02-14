package ru.imlocal.ui.favorites

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.Credentials
import ru.imlocal.data.api.Api
import ru.imlocal.data.local.DbManager
import ru.imlocal.data.local.dao.FavActionDao
import ru.imlocal.data.local.dao.FavEventDao
import ru.imlocal.data.local.dao.FavPlaceDao
import ru.imlocal.data.local.entities.FavAction
import ru.imlocal.data.local.entities.FavEvent
import ru.imlocal.data.local.entities.FavPlace
import ru.imlocal.models.FavType
import ru.imlocal.models.Favorites
import ru.imlocal.utils.getUser

class FavoritesRepository(private val apiService: Api, val context: Context) {

    private val scope = CoroutineScope(Dispatchers.IO)
    private val favActionDao: FavActionDao = DbManager.db.favActionDao()
    private val favPlaceDao: FavPlaceDao = DbManager.db.favPlaceDao()
    private val favEventDao: FavEventDao = DbManager.db.favEventDao()

    private val _favorites = MutableLiveData<Favorites>()
    val favorites: LiveData<Favorites>
        get() = _favorites

    fun getFavorites() {
        scope.launch {
            val res = apiService.getFavorites(
                Credentials.basic(getUser(context).accessToken, ""),
                getUser(context).id.toString(),
                "shopsFavorites,eventsFavorites,happeningsFavorites"
            )
            _favorites.postValue(
                Favorites(
                    res.actionsFavoritesList.associateBy({ it.id }, { it }).toMutableMap(),
                    res.placesFavoritesList.associateBy({ it.shopId }, { it }).toMutableMap(),
                    res.eventsFavoritesList.associateBy({ it.id }, { it }).toMutableMap()
                )
            )

            favPlaceDao.insertFavPlaces(res.placesFavoritesList.map { FavPlace(placeId = it.shopId) })
            favEventDao.insertFavEvents(res.eventsFavoritesList.map { FavEvent(eventId = it.id) })
            favActionDao.insertFavActions(res.actionsFavoritesList.map { FavAction(actionId = it.id) })
        }
    }

    fun deleteFavFromRecyclerView(id: Int, context: Context, type: FavType) {
        val kind: String
        val copy = _favorites.value!!
        kind = when (type) {
            FavType.PLACE -> {
                copy.placesFavoritesList?.remove(id)
                FavType.PLACE.kind
            }
            FavType.ACTION -> {
                copy.actionsFavoritesList?.remove(id)
                FavType.ACTION.kind
            }
            FavType.EVENT -> {
                copy.eventsFavoritesList?.remove(id)
                FavType.EVENT.kind
            }
        }

        scope.launch {
            apiService.removeFavorites(
                Credentials.basic(getUser(context).accessToken, ""),
                kind,
                id,
                getUser(context).id,
                ""
            ).run {
                when (kind) {
                    FavType.PLACE.kind -> favPlaceDao.deleteFavPlace(id)
                    FavType.ACTION.kind -> favActionDao.deleteFavAction(id)
                    FavType.EVENT.kind -> favEventDao.deleteFavEvent(id)
                }
            }
        }
        _favorites.value = copy
    }

    fun deleteFromFavorites(id: Int, context: Context, type: FavType) {
        val kind = when (type) {
            FavType.PLACE -> FavType.PLACE.kind
            FavType.ACTION -> FavType.ACTION.kind
            FavType.EVENT -> FavType.EVENT.kind
        }

        scope.launch {
            apiService.removeFavorites(
                Credentials.basic(getUser(context).accessToken, ""),
                kind,
                id,
                getUser(context).id,
                ""
            ).run {
                when (kind) {
                    FavType.PLACE.kind -> favPlaceDao.deleteFavPlace(id)
                    FavType.ACTION.kind -> favActionDao.deleteFavAction(id)
                    FavType.EVENT.kind -> favEventDao.deleteFavEvent(id)
                }
            }
        }

    }

    fun addToFavorite(id: Int, context: Context, type: FavType) {
        val kind = when (type) {
            FavType.PLACE -> FavType.PLACE.kind
            FavType.ACTION -> FavType.ACTION.kind
            FavType.EVENT -> FavType.EVENT.kind
        }

        scope.launch {
            apiService.addFavorites(
                Credentials.basic(getUser(context).accessToken, ""),
                kind,
                id,
                getUser(context).id
            ).run {
                if (isSuccessful) {
                    when (kind) {
                        FavType.PLACE.kind -> favPlaceDao.insertFavPlace(FavPlace(placeId = id))
                        FavType.ACTION.kind -> favActionDao.insertFavAction(FavAction(actionId = id))
                        FavType.EVENT.kind -> favEventDao.insertFavEvent(FavEvent(eventId = id))
                    }
                }
            }
        }
    }

    suspend fun isFavorite(id: Int, type: FavType): Boolean {
        var result = false
        scope.async {
            val item = when (type) {
                FavType.PLACE -> favPlaceDao.findFavPlace(id)?.placeId
                FavType.ACTION -> favActionDao.findFavAction(id)?.actionId
                FavType.EVENT -> favEventDao.findFavEvent(id)?.eventId
            }
            result = item != null
        }.await()
        return result
    }
}