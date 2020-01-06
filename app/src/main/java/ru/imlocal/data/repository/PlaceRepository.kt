package ru.imlocal.data.repository

import androidx.lifecycle.LiveData
import io.reactivex.disposables.CompositeDisposable
import ru.imlocal.data.api.Api
import ru.imlocal.models.Place

class PlaceRepository(private val apiService: Api) {

    lateinit var placeDetailsNetworkDataSource: PlaceDetailsNetworkDataSource

    fun fetchSinglePlaceDetails(
        compositeDisposable: CompositeDisposable,
        placeId: Int
    ): LiveData<Place> {
        placeDetailsNetworkDataSource =
            PlaceDetailsNetworkDataSource(apiService, compositeDisposable)
        placeDetailsNetworkDataSource.fetchPlace(placeId)

        return placeDetailsNetworkDataSource.downloadedPlaceDetailsResponse
    }

    fun getPlaceDetailsNetworkState(): LiveData<NetworkState> {
        return placeDetailsNetworkDataSource.networkState
    }
}