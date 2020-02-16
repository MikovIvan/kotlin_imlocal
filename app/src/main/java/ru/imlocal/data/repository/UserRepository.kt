package ru.imlocal.data.repository

import android.content.Context
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.imlocal.data.api.Api
import ru.imlocal.models.User
import ru.imlocal.ui.favorites.FavoritesRepository
import ru.imlocal.utils.saveUser

class UserRepository(
    private val apiService: Api,
    val context: Context
) {
    var userLogin: User = User()
    val favoritesRepository = FavoritesRepository(apiService, context)

    fun userLogin(user: User, context: Context) {
        return apiService.loginUser(user).enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.d("AUTH", "onFailure " + t.message)
            }

            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.body()?.id != -1) {
                    if (response.body()?.middleName != null) {
                        userLogin.middleName = response.body()?.middleName ?: ""
                        userLogin.id = response.body()!!.id
                        saveUser(userLogin, context)
                        favoritesRepository.getFavorites()
                    }
                } else {
                    apiService.registerUser(user).enqueue(object : Callback<User> {
                        override fun onFailure(call: Call<User>, t: Throwable) {
                            Log.d("AUTH", "onFailure " + t.message)
                        }

                        override fun onResponse(call: Call<User>, response: Response<User>) {
                            userLogin.id = response.body()!!.id
                            saveUser(userLogin, context)
                        }
                    })
                }
            }
        })
    }

    fun saveUser(
        context: Context,
        source_id: String,
        email: String,
        firstName: String,
        lastName: String,
        source: String,
        accessToken: String
    ) {
        userLogin.source_id = source_id
        userLogin.source = source
        userLogin.email = email
        userLogin.accessToken = accessToken
        userLogin.firstName = firstName
        userLogin.lastName = lastName
        userLogin.username = "$firstName $lastName"
        Log.d("AUTH", "parametrs: " + userLogin.toString())
        userLogin(userLogin, context)
        userLogin.isLogin = true
    }

}