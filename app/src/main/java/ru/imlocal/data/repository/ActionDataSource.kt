package ru.imlocal.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import retrofit2.Call
import retrofit2.Response
import ru.imlocal.data.api.Api
import ru.imlocal.data.api.FIRST_PAGE
import ru.imlocal.data.api.PER_PAGE
import ru.imlocal.models.Action

class ActionDataSource(private val apiService: Api) : PageKeyedDataSource<Int, Action>() {

    private var page = FIRST_PAGE
    private var perPage = PER_PAGE

    val networkState: MutableLiveData<NetworkState> = MutableLiveData()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Action>
    ) {
        networkState.postValue(NetworkState.LOADING)

        apiService.getAllActions(page, perPage).enqueue(
            object : retrofit2.Callback<List<Action>> {
                override fun onFailure(call: Call<List<Action>>, t: Throwable) {
                    networkState.postValue(NetworkState.ERROR)
                }

                override fun onResponse(
                    call: Call<List<Action>>,
                    response: Response<List<Action>>
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

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Action>) {
        networkState.postValue(NetworkState.LOADING)
        apiService.getAllActions(params.key, perPage).enqueue(
            object : retrofit2.Callback<List<Action>> {
                override fun onFailure(call: Call<List<Action>>, t: Throwable) {
                    networkState.postValue(NetworkState.ERROR)
                }

                override fun onResponse(
                    call: Call<List<Action>>,
                    response: Response<List<Action>>
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

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Action>) {

    }

    class ActionDataSourceFactory(private val apiService: Api) : DataSource.Factory<Int, Action>() {

        val actionLiveDataDataSource = MutableLiveData<ActionDataSource>()

        override fun create(): DataSource<Int, Action> {
            val actionDataSource = ActionDataSource(apiService)
            actionLiveDataDataSource.postValue(actionDataSource)
            return actionDataSource
        }
    }
}