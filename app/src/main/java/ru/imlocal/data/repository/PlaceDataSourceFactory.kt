package ru.imlocal.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import ru.imlocal.data.api.Api
import ru.imlocal.models.Place


class PlaceDataSourceFactory(
    private val apiService: Api
//    ,
//    private val compositeDisposable: CompositeDisposable
) : DataSource.Factory<Int, Place>() {

    val placeLiveDataDataSource = MutableLiveData<PlaceDataSource>()

    override fun create(): DataSource<Int, Place> {
        val placeDataSource = PlaceDataSource(
            apiService
//            ,compositeDisposable
        )
        placeLiveDataDataSource.postValue(placeDataSource)
        return placeDataSource
    }
}