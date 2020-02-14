package ru.imlocal.ui.places.place

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.runBlocking
import ru.imlocal.data.repository.NetworkState
import ru.imlocal.data.repository.PlaceRepository
import ru.imlocal.models.FavType
import ru.imlocal.models.Place
import ru.imlocal.ui.favorites.FavoritesRepository

class VitrinaPlaceViewModel(
    private val placeRepository: PlaceRepository,
    private val favoritesRepository: FavoritesRepository,
    placeId: Int
) : ViewModel() {

    val placeDetails: LiveData<Place> by lazy {
        placeRepository.fetchSinglePlaceDetails(placeId)
    }

    val networkState: LiveData<NetworkState> by lazy {
        placeRepository.getPlaceDetailsNetworkState()
    }

    fun addToFavorites(id: Int, context: Context, type: FavType) {
        favoritesRepository.addToFavorite(id, context, type)
    }

    fun deleteFromFavorites(id: Int, context: Context, type: FavType) {
        favoritesRepository.deleteFromFavorites(id, context, type)
    }

    fun isFavorite(id: Int, type: FavType): Boolean = runBlocking { favoritesRepository.isFavorite(id, type) }

    fun share(context: Context?) {
        Toast.makeText(context, "share", Toast.LENGTH_SHORT).show()
    }
}

