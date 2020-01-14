package ru.imlocal.ui.events

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import ru.imlocal.data.repository.NetworkState
import ru.imlocal.data.repository.PlacePagedListRepository
import ru.imlocal.models.Event

class ListEventsViewModel(private val eventRepository: PlacePagedListRepository) : ViewModel() {

    val eventPagedList: LiveData<PagedList<Event>> by lazy {
        eventRepository.fetchLiveEventPagedList()
    }

    val networkState: LiveData<NetworkState> by lazy {
        eventRepository.getNetworkState("eventDataSource")
    }

    fun listIsEmpty(): Boolean {
        return eventPagedList.value?.isEmpty() ?: true
    }

}
