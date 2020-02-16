package ru.imlocal.utils

import android.content.Context
import androidx.preference.PreferenceManager

const val TAB = "tab"

fun saveTab(tab: Int, context: Context?) {
    val prefs = PreferenceManager.getDefaultSharedPreferences(context)
    val prefsEditor = prefs.edit()
    prefsEditor.putInt(TAB, tab)
    prefsEditor.apply()
}

fun getTab(context: Context?): Int {
    val prefs = PreferenceManager.getDefaultSharedPreferences(context)
    return prefs.getInt(TAB, 0)
}

