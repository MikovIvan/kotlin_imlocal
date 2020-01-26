package ru.imlocal.extensions

import android.content.Context
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat
import ru.imlocal.R

fun ActionBar.setup(context: Context, title: String = "", backgroundColor: Int, icon: String) {
    setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(context, backgroundColor)))
    setTitle("")
    when (icon) {
        "icon" -> setIcon(R.drawable.ic_toolbar_icon)
        else -> setIcon(ColorDrawable(ContextCompat.getColor(context, android.R.color.transparent)))
    }
}