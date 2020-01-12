package ru.imlocal.ui.places.place

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.imlocal.data.repository.NetworkState
import ru.imlocal.data.repository.PlaceRepository
import ru.imlocal.models.Place

class VitrinaPlaceViewModel(private val placeRepository: PlaceRepository, placeId: Int) :
    ViewModel() {

    val placeDetails: LiveData<Place> by lazy {
        placeRepository.fetchSinglePlaceDetails(placeId)
    }

    val networkState: LiveData<NetworkState> by lazy {
        placeRepository.getPlaceDetailsNetworkState()
    }

}

