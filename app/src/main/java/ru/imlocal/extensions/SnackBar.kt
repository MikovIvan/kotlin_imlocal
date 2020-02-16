package ru.imlocal.extensions

import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
import ru.imlocal.R

fun View.showLoginSnackbar(
    snackbarText: String = resources.getString(R.string.need_login),
    timeLength: Int = Snackbar.LENGTH_LONG,
    fragment: Fragment,
    action: NavDirections
) {
    Snackbar.make(this, snackbarText, timeLength)
        .setAction(resources.getString(R.string.login)) {
            NavHostFragment.findNavController(fragment).navigate(action)
        }
        .show()
}

fun View.showSnackbar(
    snackbarText: String,
    timeLength: Int = Snackbar.LENGTH_SHORT
) {
    Snackbar.make(this, snackbarText, timeLength).show()
}