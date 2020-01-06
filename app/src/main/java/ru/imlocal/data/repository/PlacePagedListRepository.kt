package ru.imlocal.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import ru.imlocal.data.api.Api
import ru.imlocal.models.Place

class PlacePagedListRepository(private val apiService: Api) {

    lateinit var placePagedList: LiveData<PagedList<Place>>
    lateinit var placesDataSourceFactory: PlaceDataSourceFactory

    fun fetchLivePlacePagedList(): LiveData<PagedList<Place>> {
        placesDataSourceFactory = PlaceDataSourceFactory(apiService)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(5)
            .build()

        placePagedList = LivePagedListBuilder(placesDataSourceFactory, config).build()

        return placePagedList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<PlaceDataSource, NetworkState>(
            placesDataSourceFactory.placeLiveDataDataSource, PlaceDataSource::networkState
        )
    }
}