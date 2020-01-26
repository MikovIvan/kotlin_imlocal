package ru.imlocal.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.imlocal.extensions.getUser
import ru.imlocal.models.User

class MainViewModel(val app: Application) : AndroidViewModel(app) {

    fun getCurrentUser(context: Context): MutableLiveData<User> {
        val user: MutableLiveData<User> = MutableLiveData(User())
        user.postValue(getUser(context))
        return user
    }

    fun syncDataIfNeed(): LiveData<LoadResult<Boolean>> {
        val result: MutableLiveData<LoadResult<Boolean>> =
            MutableLiveData(LoadResult.Loading(false))

        viewModelScope.launch(Dispatchers.IO) {
            delay(1000)
            result.postValue(LoadResult.Success(true))
        }

        return result
    }
}

sealed class LoadResult<T>(
    val data: T?,
    val errorMessage: String? = null
) {
    class Success<T>(data: T?) : LoadResult<T>(data)
    class Loading<T>(data: T? = null) : LoadResult<T>(data)
    class Error<T>(errorMessage: String?, data: T? = null) : LoadResult<T>(data, errorMessage)
}