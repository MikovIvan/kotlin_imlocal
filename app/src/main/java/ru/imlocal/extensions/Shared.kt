package ru.imlocal.extensions

import android.content.Context
import androidx.preference.PreferenceManager

import com.google.gson.Gson
import ru.imlocal.models.User

const val USER = "user"

fun saveUser(user: User, context: Context?) {
    val prefs = PreferenceManager.getDefaultSharedPreferences(context)
    val prefsEditor = prefs.edit()
    val gson = Gson()
    val json = gson.toJson(user)
    prefsEditor.putString(USER, json)
    prefsEditor.apply()
}

fun getUser(context: Context): User {
    val prefs = PreferenceManager.getDefaultSharedPreferences(context)
    val gson = Gson()
    val json = prefs.getString(USER, "")
    return gson.fromJson(json, User::class.java)
}

