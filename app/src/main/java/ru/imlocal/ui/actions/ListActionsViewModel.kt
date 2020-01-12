package ru.imlocal.ui.actions

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import ru.imlocal.data.repository.NetworkState
import ru.imlocal.data.repository.PlacePagedListRepository
import ru.imlocal.models.Action

class ListActionsViewModel(private val actionRepository: PlacePagedListRepository) : ViewModel() {

    val actionPagedList: LiveData<PagedList<Action>> by lazy {
        actionRepository.fetchLiveActionPagedList()
    }

    val networkState: LiveData<NetworkState> by lazy {
        actionRepository.getNetworkState("actionDataSource")
    }

    fun listIsEmpty(): Boolean {
        return actionPagedList.value?.isEmpty() ?: true
    }

}
