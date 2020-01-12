package ru.imlocal.data.repository

import androidx.lifecycle.LiveData
import ru.imlocal.data.api.Api
import ru.imlocal.models.Action

class ActionRepository(private val apiService: Api) {

    lateinit var actionDetailsNetworkDataSource: DetailsNetworkDataSource

    fun fetchSingleActionDetails(
        placeId: Int
    ): LiveData<Action> {
        actionDetailsNetworkDataSource =
            DetailsNetworkDataSource(apiService)
        actionDetailsNetworkDataSource.fetchAction(placeId)

        return actionDetailsNetworkDataSource.downloadedActionDetailsResponse
    }

    fun getPlaceDetailsNetworkState(): LiveData<NetworkState> {
        return actionDetailsNetworkDataSource.networkState
    }
}