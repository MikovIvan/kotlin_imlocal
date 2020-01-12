package ru.imlocal.ui.events.event

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.imlocal.data.repository.EventRepository
import ru.imlocal.data.repository.NetworkState
import ru.imlocal.models.Event

class VitrinaEventViewModel(private val eventRepository: EventRepository, eventId: Int) :
    ViewModel() {

    val actionDetails: LiveData<Event> by lazy {
        eventRepository.fetchSingleEventDetails(eventId)
    }

    val networkState: LiveData<NetworkState> by lazy {
        eventRepository.getEventDetailsNetworkState()
    }

}
