package ru.imlocal.ui.actions.action

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.runBlocking
import ru.imlocal.data.repository.ActionRepository
import ru.imlocal.data.repository.NetworkState
import ru.imlocal.models.Action
import ru.imlocal.models.FavType
import ru.imlocal.ui.favorites.FavoritesRepository

class VitrinaActionViewModel(
    private val actionRepository: ActionRepository,
    private val favoritesRepository: FavoritesRepository,
    actionId: Int
) : ViewModel() {

    val actionDetails: LiveData<Action> by lazy {
        actionRepository.fetchSingleActionDetails(actionId)
    }

    val networkState: LiveData<NetworkState> by lazy {
        actionRepository.getPlaceDetailsNetworkState()
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
