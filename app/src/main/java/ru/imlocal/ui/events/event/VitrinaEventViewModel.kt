package ru.imlocal.ui.events.event

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.runBlocking
import ru.imlocal.data.repository.EventRepository
import ru.imlocal.data.repository.NetworkState
import ru.imlocal.models.Event
import ru.imlocal.models.FavType
import ru.imlocal.ui.favorites.FavoritesRepository

class VitrinaEventViewModel(
    private val eventRepository: EventRepository,
    private val favoritesRepository: FavoritesRepository,
    eventId: Int
) : ViewModel() {

    val actionDetails: LiveData<Event> by lazy {
        eventRepository.fetchSingleEventDetails(eventId)
    }

    val networkState: LiveData<NetworkState> by lazy {
        eventRepository.getEventDetailsNetworkState()
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
