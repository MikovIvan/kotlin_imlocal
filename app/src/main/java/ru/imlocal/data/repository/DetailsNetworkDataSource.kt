package ru.imlocal.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.imlocal.data.api.Api
import ru.imlocal.models.Action
import ru.imlocal.models.Event
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

    private val _downloadedEventDetailsResponse = MutableLiveData<Event>()
    val downloadedEventDetailsResponse: LiveData<Event>
        get() = _downloadedEventDetailsResponse

    fun fetchPlace(placeId: Int) {
        _networkState.postValue(NetworkState.LOADING)

        apiService.getShop(placeId).enqueue(object : Callback<Place> {
            override fun onFailure(call: Call<Place>, t: Throwable) {
                _networkState.postValue(NetworkState.ERROR)
            }

            override fun onResponse(call: Call<Place>, response: Response<Place>) {
                if (response.isSuccessful) {
                    _downloadedPlaceDetailsResponse.postValue(response.body())
                    _networkState.postValue(NetworkState.LOADED)
                } else {
                    _networkState.postValue(NetworkState.ERROR)
                }
            }
        })
    }

    fun fetchAction(actionId: Int) {
        _networkState.postValue(NetworkState.LOADING)

        apiService.getAction(actionId).enqueue(object : Callback<Action> {
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

    fun fetchEvent(eventId: Int) {
        _networkState.postValue(NetworkState.LOADING)

        apiService.getEvent(eventId).enqueue(object : Callback<Event> {
            override fun onFailure(call: Call<Event>, t: Throwable) {
                _networkState.postValue(NetworkState.ERROR)
            }

            override fun onResponse(call: Call<Event>, response: Response<Event>) {
                if (response.isSuccessful) {
                    _downloadedEventDetailsResponse.postValue(response.body())
                    _networkState.postValue(NetworkState.LOADED)
                } else {
                    _networkState.postValue(NetworkState.ERROR)
                }
            }

        })
    }
}