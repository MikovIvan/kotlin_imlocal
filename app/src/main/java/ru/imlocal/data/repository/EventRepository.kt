package ru.imlocal.data.repository

import androidx.lifecycle.LiveData
import ru.imlocal.data.api.Api
import ru.imlocal.models.Event

class EventRepository(private val apiService: Api) {

    lateinit var eventDetailsNetworkDataSource: DetailsNetworkDataSource

    fun fetchSingleEventDetails(
        eventId: Int
    ): LiveData<Event> {
        eventDetailsNetworkDataSource =
            DetailsNetworkDataSource(apiService)
        eventDetailsNetworkDataSource.fetchEvent(eventId)

        return eventDetailsNetworkDataSource.downloadedEventDetailsResponse
    }

    fun getEventDetailsNetworkState(): LiveData<NetworkState> {
        return eventDetailsNetworkDataSource.networkState
    }
}