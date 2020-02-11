package ru.imlocal.ui.favorites

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import okhttp3.Credentials
import ru.imlocal.data.api.Api
import ru.imlocal.extensions.getUser
import ru.imlocal.models.FavType
import ru.imlocal.models.Favorites

class FavoritesRepository(private val apiService: Api, val context: Context) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

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
        }
    }

    fun deletePlaceFromFavorites(id: Int, context: Context, type: FavType) {
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
            )
        }
        _favorites.value = copy
    }

}