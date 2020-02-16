package ru.imlocal.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vk.sdk.VKSdk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.imlocal.data.repository.UserRepository
import ru.imlocal.models.User
import ru.imlocal.ui.favorites.FavoritesRepository
import ru.imlocal.utils.getUser

class MainViewModel(
    val app: Application,
    private val favoritesRepository: FavoritesRepository,
    private val userRepository: UserRepository
) : AndroidViewModel(app) {

    fun getCurrentUser(context: Context): LiveData<User> {
//        return userRepository.getCurrentUser()
        val user: MutableLiveData<User> = MutableLiveData(User())
        if (VKSdk.isLoggedIn()) user.postValue(getUser(context)) else user.postValue(User())
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

    fun getFavorites() {
        favoritesRepository.getFavorites()
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