package ru.imlocal.ui.actions.action

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.imlocal.data.repository.ActionRepository
import ru.imlocal.data.repository.NetworkState
import ru.imlocal.models.Action

class VitrinaActionViewModel(private val actionRepository: ActionRepository, actionId: Int) :
    ViewModel() {

    val actionDetails: LiveData<Action> by lazy {
        actionRepository.fetchSingleActionDetails(actionId)
    }

    val networkState: LiveData<NetworkState> by lazy {
        actionRepository.getPlaceDetailsNetworkState()
    }

}
