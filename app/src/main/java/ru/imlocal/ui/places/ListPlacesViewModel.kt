package ru.imlocal.ui.places

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import ru.imlocal.data.repository.NetworkState
import ru.imlocal.data.repository.PlacePagedListRepository
import ru.imlocal.models.Place

class ListPlacesViewModel(private val placeRepository: PlacePagedListRepository) : ViewModel() {

    val placePagedList: LiveData<PagedList<Place>> by lazy {
        placeRepository.fetchLivePlacePagedList()
    }

    val networkState: LiveData<NetworkState> by lazy {
        placeRepository.getNetworkState("placeDataSource")
    }

    fun listIsEmpty(): Boolean {
        return placePagedList.value?.isEmpty() ?: true
    }

}