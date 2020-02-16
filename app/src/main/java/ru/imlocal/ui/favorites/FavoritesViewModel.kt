package ru.imlocal.ui.favorites

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.imlocal.models.FavType
import ru.imlocal.models.Favorites

class FavoritesViewModel(private val favoritesRepository: FavoritesRepository) : ViewModel() {

    fun deleteFromFavorites(id: Int, context: Context, type: FavType) {
        favoritesRepository.deleteFavFromRecyclerView(id, context, type)
    }

    val favoritesPlaces: LiveData<Favorites> by lazy {
        favoritesRepository.getFavorites()
        favoritesRepository.favorites
    }

}
