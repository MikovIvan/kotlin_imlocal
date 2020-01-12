package ru.imlocal.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import ru.imlocal.data.api.Api
import ru.imlocal.data.api.PER_PAGE
import ru.imlocal.data.repository.ActionDataSource.ActionDataSourceFactory
import ru.imlocal.models.Action
import ru.imlocal.models.Place

class PlacePagedListRepository(private val apiService: Api) {

    lateinit var placePagedList: LiveData<PagedList<Place>>
    lateinit var placesDataSourceFactory: PlaceDataSourceFactory
    lateinit var actionPagedList: LiveData<PagedList<Action>>
    lateinit var actionsDataSourceFactory: ActionDataSourceFactory

    fun fetchLivePlacePagedList(): LiveData<PagedList<Place>> {
        placesDataSourceFactory = PlaceDataSourceFactory(apiService)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(PER_PAGE)
            .build()

        placePagedList = LivePagedListBuilder(placesDataSourceFactory, config).build()

        return placePagedList
    }

    fun fetchLiveActionPagedList(): LiveData<PagedList<Action>> {
        actionsDataSourceFactory = ActionDataSourceFactory(apiService)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(PER_PAGE)
            .build()

        actionPagedList = LivePagedListBuilder(actionsDataSourceFactory, config).build()

        return actionPagedList
    }

    fun getNetworkState(dataSource: String): LiveData<NetworkState> {
        return when (dataSource) {
            "placeDataSource" -> {
                return Transformations.switchMap<PlaceDataSource, NetworkState>(
                    placesDataSourceFactory.placeLiveDataDataSource, PlaceDataSource::networkState
                )
            }
//            "actionDataSource" -> {
            else -> {
                return Transformations.switchMap<ActionDataSource, NetworkState>(
                    actionsDataSourceFactory.actionLiveDataDataSource,
                    ActionDataSource::networkState
                )
            }
        }
    }

}