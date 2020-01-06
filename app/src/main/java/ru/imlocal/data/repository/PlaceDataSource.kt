package ru.imlocal.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import retrofit2.Call
import retrofit2.Response
import ru.imlocal.data.api.Api
import ru.imlocal.data.api.FIRST_PAGE
import ru.imlocal.models.Place

class PlaceDataSource(
    private val apiService: Api
//    ,
//    private val compositeDisposable: CompositeDisposable
) : PageKeyedDataSource<Int, Place>() {

    private var page = FIRST_PAGE
    private var userPoint = "55.7655,37.4693"
    private var range = 1000000
    private var perPage = 5


    val networkState: MutableLiveData<NetworkState> = MutableLiveData()


    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Place>
    ) {
        networkState.postValue(NetworkState.LOADING)

        apiService.getAllShops(userPoint, range, page, perPage).enqueue(
            object : retrofit2.Callback<List<Place>> {
                override fun onFailure(call: Call<List<Place>>, t: Throwable) {
                    networkState.postValue(NetworkState.ERROR)
                }

                override fun onResponse(call: Call<List<Place>>, response: Response<List<Place>>) {
                    if (response.isSuccessful) {
                        val data = response.body()
                        val items = data?.map { it } ?: emptyList()
                        callback.onResult(items, null, page + 1)
                        networkState.postValue(NetworkState.LOADED)
                    } else {
                        networkState.postValue(NetworkState.ERROR)
                    }
                }

            }
        )
//        compositeDisposable.add(
//            apiService.getAllShops(userPoint, range, page, perPage)
//                .subscribeOn(Schedulers.io())
//                .subscribe(
//                    {
//                        callback.onResult(it.body() , null, page + 1)
//                        networkState.postValue(NetworkState.LOADED)
//                    },
//                    {
//                        networkState.postValue(NetworkState.ERROR)
//                        Log.e("PlaceDataSource", it.message)
//                    }
//                )
//        )
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, Place>
    ) {
        networkState.postValue(NetworkState.LOADING)
        apiService.getAllShops(userPoint, range, params.key, perPage).enqueue(
            object : retrofit2.Callback<List<Place>> {
                override fun onFailure(call: Call<List<Place>>, t: Throwable) {
                    networkState.postValue(NetworkState.ERROR)
                }

                override fun onResponse(call: Call<List<Place>>, response: Response<List<Place>>) {
                    if (response.isSuccessful) {
                        val data = response.body()
                        val items = data?.map { it } ?: emptyList()
                        if (response.headers().get("X-Pagination-Page-Count").toString().toInt() >= params.key) {
                            callback.onResult(items, params.key + 1)
                        } else {
                            networkState.postValue(NetworkState.ENDOFLIST)
                        }
                        networkState.postValue(NetworkState.LOADED)
                    } else {
                        networkState.postValue(NetworkState.ERROR)
                    }
                }

            }
        )
//        compositeDisposable.add(
//            apiService.getAllShops(userPoint, range, params.key, perPage)
//                .subscribeOn(Schedulers.io())
//                .subscribe(
//                    {
//                        if (it.headers("X-Pagination-Page-Count").toString().toInt() >= params.key) {
//                            callback.onResult(it.body() as List<Place>, params.key + 1)
//                        } else {
//                            networkState.postValue(NetworkState.ENDOFLIST)
//                        }
//                        networkState.postValue(NetworkState.LOADED)
//                    },
//                    {
//                        networkState.postValue(NetworkState.ERROR)
//                        Log.e("PlaceDataSource", it.message)
//                    }
//                )
//        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Place>) {

    }
}


