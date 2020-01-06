package ru.imlocal.ui.places.place

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import ru.imlocal.data.repository.NetworkState
import ru.imlocal.data.repository.PlaceRepository
import ru.imlocal.models.Place

class VitrinaPlaceViewModel(private val placeRepository: PlaceRepository, placeId: Int) :
    ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    val placeDetails: LiveData<Place> by lazy {
        placeRepository.fetchSinglePlaceDetails(compositeDisposable, placeId)
    }

    val networkState: LiveData<NetworkState> by lazy {
        placeRepository.getPlaceDetailsNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}

