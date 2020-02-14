package ru.imlocal.ui.actions

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import kotlinx.coroutines.runBlocking
import ru.imlocal.data.repository.NetworkState
import ru.imlocal.data.repository.PlacePagedListRepository
import ru.imlocal.models.Action
import ru.imlocal.models.FavType
import ru.imlocal.ui.favorites.FavoritesRepository

class ListActionsViewModel(
    private val actionRepository: PlacePagedListRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    val actionPagedList: LiveData<PagedList<Action>> by lazy {
        actionRepository.fetchLiveActionPagedList()
    }

    val networkState: LiveData<NetworkState> by lazy {
        actionRepository.getNetworkState("actionDataSource")
    }

    fun listIsEmpty(): Boolean {
        return actionPagedList.value?.isEmpty() ?: true
    }

    fun share(context: Context?) {
        Toast.makeText(context, "share", Toast.LENGTH_SHORT).show()
    }

    fun addToFavorites(id: Int, context: Context, type: FavType) {
        favoritesRepository.addToFavorite(id, context, type)
    }

    fun deleteFromFavorites(id: Int, context: Context, type: FavType) {
        favoritesRepository.deleteFromFavorites(id, context, type)
    }

    fun isFavorite(id: Int, type: FavType): Boolean = runBlocking { favoritesRepository.isFavorite(id, type) }

}
