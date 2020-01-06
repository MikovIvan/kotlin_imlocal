package ru.imlocal.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.imlocal.data.api.Api
import ru.imlocal.models.Place

class PlaceDetailsNetworkDataSource(
    private val apiService: Api,
    private val compositeDisposable: CompositeDisposable
) {

    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    private val _downloadedPlaceDetailsResponse = MutableLiveData<Place>()
    val downloadedPlaceDetailsResponse: LiveData<Place>
        get() = _downloadedPlaceDetailsResponse

    fun fetchPlace(placeId: Int) {
        _networkState.postValue(NetworkState.LOADING)

        try {
            compositeDisposable.add(
                apiService.getShop(placeId)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            _downloadedPlaceDetailsResponse.postValue(it)
                            _networkState.postValue(NetworkState.LOADED)
                        },
                        {
                            _networkState.postValue(NetworkState.ERROR)
                            Log.e("PlaceDetailsDataSource", it.message!!)
                        }
                    )
            )
        } catch (e: Exception) {
            Log.e("PlaceDetailsDataSource", e.message!!)
        }
    }
}