package ru.imlocal.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.imlocal.data.api.Api
import ru.imlocal.models.Action
import ru.imlocal.models.Place

class DetailsNetworkDataSource(
    private val apiService: Api
) {

    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    private val _downloadedPlaceDetailsResponse = MutableLiveData<Place>()
    val downloadedPlaceDetailsResponse: LiveData<Place>
        get() = _downloadedPlaceDetailsResponse

    private val _downloadedActionDetailsResponse = MutableLiveData<Action>()
    val downloadedActionDetailsResponse: LiveData<Action>
        get() = _downloadedActionDetailsResponse

    fun fetchPlace(placeId: Int) {
        _networkState.postValue(NetworkState.LOADING)

        apiService.getShop(placeId).enqueue(object : Callback<Place> {
            override fun onFailure(call: Call<Place>, t: Throwable) {
                _networkState.postValue(NetworkState.ERROR)
            }

            override fun onResponse(call: Call<Place>, response: Response<Place>) {
                _downloadedPlaceDetailsResponse.postValue(response.body())
                _networkState.postValue(NetworkState.LOADED)
            }
        })
    }

    fun fetchAction(actionId: Int) {
        _networkState.postValue(NetworkState.LOADING)

        apiService.getAction(actionId).enqueue(object : retrofit2.Callback<Action> {
            override fun onFailure(call: Call<Action>, t: Throwable) {
                _networkState.postValue(NetworkState.ERROR)
            }

            override fun onResponse(call: Call<Action>, response: Response<Action>) {
                if (response.isSuccessful) {
                    _downloadedActionDetailsResponse.postValue(response.body())
                    _networkState.postValue(NetworkState.LOADED)
                } else {
                    _networkState.postValue(NetworkState.ERROR)
                }
            }

        })
    }
}