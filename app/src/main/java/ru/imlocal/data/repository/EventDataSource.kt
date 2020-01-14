package ru.imlocal.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import retrofit2.Call
import retrofit2.Response
import ru.imlocal.data.api.Api
import ru.imlocal.data.api.FIRST_PAGE
import ru.imlocal.data.api.PER_PAGE
import ru.imlocal.models.Event

class EventDataSource(private val apiService: Api) : PageKeyedDataSource<Int, Event>() {

    private var page = FIRST_PAGE
    private var perPage = PER_PAGE

    val networkState: MutableLiveData<NetworkState> = MutableLiveData()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Event>
    ) {
        networkState.postValue(NetworkState.LOADING)

        apiService.getAllEvents(page, perPage).enqueue(
            object : retrofit2.Callback<List<Event>> {
                override fun onFailure(call: Call<List<Event>>, t: Throwable) {
                    networkState.postValue(NetworkState.ERROR)
                }

                override fun onResponse(
                    call: Call<List<Event>>,
                    response: Response<List<Event>>
                ) {
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
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Event>) {
        networkState.postValue(NetworkState.LOADING)
        apiService.getAllEvents(params.key, perPage).enqueue(
            object : retrofit2.Callback<List<Event>> {
                override fun onFailure(call: Call<List<Event>>, t: Throwable) {
                    networkState.postValue(NetworkState.ERROR)
                }

                override fun onResponse(
                    call: Call<List<Event>>,
                    response: Response<List<Event>>
                ) {
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
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Event>) {

    }

    class EventDataSourceFactory(private val apiService: Api) : DataSource.Factory<Int, Event>() {

        val eventLiveDataDataSource = MutableLiveData<EventDataSource>()

        override fun create(): DataSource<Int, Event> {
            val eventDataSource = EventDataSource(apiService)
            eventLiveDataDataSource.postValue(eventDataSource)
            return eventDataSource
        }
    }
}
