package ru.imlocal.ui

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewOutlineProvider
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController

import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import ru.imlocal.R
import ru.imlocal.ui.splash.FragmentSplashDirections

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        setSupportActionBar(activity_main_toolbar)

        navController = findNavController(R.id.nav_host)
        findViewById<NavigationView>(R.id.nav_view)
            .setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination: NavDestination, _ ->
            when (destination.id) {
                R.id.fragment_vitrina_action -> {
                    setToolbar(true)
                }
                R.id.fragment_vitrina_place -> {
                    setToolbar(false)
                }
                R.id.fragment_vitrina_event -> {
                    setToolbar(false)
                }
                R.id.fragment_main -> {
                    setToolbar(true)
                    activity_main_appbar.visibility = View.VISIBLE
                }
                R.id.fragment_splash -> {
                    setToolbar(false)
                    activity_main_appbar.visibility = View.INVISIBLE
                }
                R.id.fragment_map -> {
                    setToolbar(false)
                }
            }
        }

        appBarConfiguration = AppBarConfiguration(navController.graph, drawer_layout)

        setupActionBar(navController, appBarConfiguration)
        setupNavigationMenu(navController)

        savedInstanceState ?: prepareData()
    }

    private fun setupNavigationMenu(navController: NavController) {
        val sideNavView = findViewById<NavigationView>(R.id.nav_view)
        sideNavView?.setupWithNavController(navController)
    }

    private fun setupActionBar(navController: NavController, appBarConfig: AppBarConfiguration) {
        setupActionBarWithNavController(navController, appBarConfig)
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host).navigateUp(appBarConfiguration)
    }

    private fun setToolbar(isVisible: Boolean) {
        if (isVisible) {
            supportActionBar!!.setBackgroundDrawable(
                ColorDrawable(
                    ContextCompat.getColor(
                        this,
                        R.color.color_background
                    )
                )
            )
            supportActionBar!!.setTitle("")
            supportActionBar!!.setIcon(R.drawable.ic_toolbar_icon)
            activity_main_appbar.outlineProvider = ViewOutlineProvider.BACKGROUND
        } else {
            supportActionBar!!.setBackgroundDrawable(
                ColorDrawable(
                    ContextCompat.getColor(
                        this,
                        android.R.color.transparent
                    )
                )
            )
            supportActionBar!!.setTitle("")
            supportActionBar!!.setIcon(
                ColorDrawable(
                    ContextCompat.getColor(
                        this,
                        android.R.color.transparent
                    )
                )
            )
            activity_main_appbar.outlineProvider = null
        }

    }

    private fun prepareData() {
        viewModel.syncDataIfNeed().observe(this, Observer {
            when (it) {
                is LoadResult.Loading -> navController.navigate(R.id.fragment_splash)

                is LoadResult.Success -> {
                    val action =
                        FragmentSplashDirections.actionFragmentSplashToFragmentMain()
                    navController.navigate(action)
                }

                is LoadResult.Error -> {
                    Snackbar.make(
                        drawer_layout,
                        it.errorMessage.toString(),
                        Snackbar.LENGTH_INDEFINITE
                    ).show()
                }
            }
        })
    }
}


