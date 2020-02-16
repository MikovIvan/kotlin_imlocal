package ru.imlocal.data.repository

import android.content.SharedPreferences
import android.util.Log
import androidx.preference.PreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.imlocal.App
import ru.imlocal.data.api.Api
import ru.imlocal.models.User
import ru.imlocal.ui.favorites.FavoritesRepository

object UserRepository {
    private const val FIRST_NAME = "FIRST_NAME"
    private const val LAST_NAME = "LAST_NAME"
    private const val MIDDLE_NAME = "MIDDLE_NAME"
    private const val SOURCE_ID = "SOURCE_ID"
    private const val SOURCE = "SOURCE"
    private const val EMAIL = "EMAIL"
    private const val ACCESS_TOKEN = "ACCESS_TOKEN"
    private const val USER_NAME = "USER_NAME"
    private const val IS_LOGIN = "IS_LOGIN"
    private const val ID = "ID"

    val apiService: Api = Api.getClient()
    var userLogin: User = User()
    val favoritesRepository = FavoritesRepository(apiService)

    private val prefs: SharedPreferences by lazy {
        val ctx = App.applicationContext()
        PreferenceManager.getDefaultSharedPreferences(ctx)
    }

    fun userLogin(user: User) {
        return apiService.loginUser(user).enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.d("AUTH", "onFailure " + t.message)
            }

            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.body()?.id != -1) {
                    if (response.body()?.middleName != null) {
                        userLogin.middleName = response.body()?.middleName ?: ""
                        userLogin.id = response.body()!!.id
                        saveUser(userLogin)
                        favoritesRepository.getFavorites()
                    }
                } else {
                    apiService.registerUser(user).enqueue(object : Callback<User> {
                        override fun onFailure(call: Call<User>, t: Throwable) {
                            Log.d("AUTH", "onFailure " + t.message)
                        }

                        override fun onResponse(call: Call<User>, response: Response<User>) {
                            userLogin.id = response.body()!!.id
                            saveUser(userLogin)
                        }
                    })
                }
            }
        })
    }

    fun saveUser(
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
        userLogin(userLogin)
        userLogin.isLogin = true
    }

    fun saveUser(user: User) {
        with(user) {
            putValue(FIRST_NAME to firstName)
            putValue(LAST_NAME to lastName)
            putValue(MIDDLE_NAME to middleName)
            putValue(SOURCE_ID to source_id)
            putValue(SOURCE to source)
            putValue(EMAIL to email)
            putValue(ACCESS_TOKEN to accessToken)
            putValue(USER_NAME to username)
            putValue(IS_LOGIN to isLogin)
            putValue(ID to id)
        }
    }

    fun getUser() = User(
        id = prefs.getInt(ID, -1),
        source_id = prefs.getString(SOURCE_ID, "")!!,
        email = prefs.getString(EMAIL, "")!!,
        firstName = prefs.getString(FIRST_NAME, "")!!,
        lastName = prefs.getString(LAST_NAME, "")!!,
        middleName = prefs.getString(MIDDLE_NAME, "")!!,
        source = prefs.getString(SOURCE, "")!!,
        accessToken = prefs.getString(ACCESS_TOKEN, "")!!,
        username = prefs.getString(USER_NAME, "")!!,
        isLogin = prefs.getBoolean(IS_LOGIN, false)
    )

    private fun putValue(pair: Pair<String, Any>) = with(prefs.edit()) {
        val key = pair.first
        val value = pair.second

        when (value) {
            is String -> putString(key, value)
            is Int -> putInt(key, value)
            is Boolean -> putBoolean(key, value)
            is Long -> putLong(key, value)
            is Float -> putFloat(key, value)
            else -> error("only primitives types can be stored in Shared Preferences")
        }

        apply()
    }

}