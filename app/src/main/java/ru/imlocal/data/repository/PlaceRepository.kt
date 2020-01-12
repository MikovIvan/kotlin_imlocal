package ru.imlocal.data.repository

import androidx.lifecycle.LiveData
import ru.imlocal.data.api.Api
import ru.imlocal.models.Place

class PlaceRepository(private val apiService: Api) {

    lateinit var placeDetailsNetworkDataSource: DetailsNetworkDataSource

    fun fetchSinglePlaceDetails(
        placeId: Int
    ): LiveData<Place> {
        placeDetailsNetworkDataSource =
            DetailsNetworkDataSource(apiService)
        placeDetailsNetworkDataSource.fetchPlace(placeId)

        return placeDetailsNetworkDataSource.downloadedPlaceDetailsResponse
    }

    fun getPlaceDetailsNetworkState(): LiveData<NetworkState> {
        return placeDetailsNetworkDataSource.networkState
    }
}