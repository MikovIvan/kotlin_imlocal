package ru.imlocal.ui.places

import androidx.lifecycle.*
import androidx.paging.PagedList
import ru.imlocal.data.repository.NetworkState
import ru.imlocal.data.repository.PlacePagedListRepository
import ru.imlocal.extensions.mutableLiveData
import ru.imlocal.models.Place

class ListPlacesViewModel(private val placeRepository: PlacePagedListRepository) : ViewModel() {

    private val query = mutableLiveData("")
    private val placesList = mutableLiveData(placeRepository.fetchLivePlacePagedList())

    val state: MediatorLiveData<State> = MediatorLiveData<State>().apply {
        value = State()
    }
    val currentState
        get() = state.value!!

    val placePagedList: LiveData<PagedList<Place>> by lazy {
        placeRepository.fetchLivePlacePagedList()
    }

    val networkState: LiveData<NetworkState> by lazy {
        placeRepository.getNetworkState("placeDataSource")
    }

    fun listIsEmpty(): Boolean {
        return placePagedList.value?.isEmpty() ?: true
    }

    fun handleSearch(queryStr: String?) {
        query.value = queryStr.orEmpty()
    }

    fun handleSortMenu() {
        updateState { it.copy(isSortOpen = !it.isSortOpen) }
    }

    fun updateState(update: (currentState: State) -> State) {
        val updatedState: State = update(currentState)
        state.value = updatedState
    }

    fun observeState(owner: LifecycleOwner, onChanged: (newState: State) -> Unit) {
        state.observe(owner, Observer { onChanged(it!!) })
    }
}

data class State(
    val isSortOpen: Boolean = false
)