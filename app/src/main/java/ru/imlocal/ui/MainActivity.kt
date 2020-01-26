package ru.imlocal.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.vk.sdk.VKSdk
import kotlinx.android.synthetic.main.activity_main.*
import ru.imlocal.R
import ru.imlocal.extensions.getUser
import ru.imlocal.extensions.saveUser
import ru.imlocal.extensions.setup
import ru.imlocal.ui.splash.FragmentSplashDirections


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var viewModel: MainViewModel

    override fun onStart() {
        if (VKSdk.isLoggedIn()) {
            enter.title = getUser(this)?.username
            favorites.isVisible = true
            logout.isVisible = true
        }
        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.getCurrentUser(this).observe(this, Observer {
            if (it != null) {
                if (it.isLogin) {
                    enter.title = it.username
                    favorites.isVisible = true
                    logout.isVisible = true
                }
            } else {
                enter.title = getString(R.string.menu_item_drawer_login)
                favorites.isVisible = false
                logout.isVisible = false
            }
        })

        setSupportActionBar(activity_main_toolbar)

        navController = findNavController(R.id.nav_host)

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
                R.id.fragment_login -> {
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
//        nav_view.setupWithNavController(navController)
        nav_view.setNavigationItemSelectedListener(object : NavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                drawer_layout.closeDrawer(GravityCompat.START)
                when (item.itemId) {
                    R.id.fragment_login -> {
                        if (getUser(this@MainActivity) != null && getUser(this@MainActivity)?.isLogin!!) {
                            navController.navigate(R.id.fragment_profile)
                        } else {
                            navController.navigate(R.id.fragment_login)
                        }
                    }
                    R.id.fragment_favorites -> {
                        navController.navigate(R.id.fragment_favorites)
                    }
                    R.id.fragment_business -> {
                        navController.navigate(R.id.fragment_business)
                    }
                    R.id.fragment_feedback -> {
                        navController.navigate(R.id.fragment_feedback)
                    }
                    R.id.nav_logout -> {
                        viewModel.getCurrentUser(this@MainActivity).observe(this@MainActivity, Observer {
                            val user = it
                            user.isLogin = false
                            saveUser(user, this@MainActivity)
                            enter.title = getString(R.string.menu_item_drawer_login)
                            favorites.isVisible = false
                            logout.isVisible = false
                        })
                        Toast.makeText(baseContext, "LOGOuT", Toast.LENGTH_SHORT).show()
                    }
                }
                return true
            }
        })

        enter = nav_view.menu.findItem(R.id.fragment_login)
        favorites = nav_view.menu.findItem(R.id.fragment_favorites)
        logout = nav_view.menu.findItem(R.id.nav_logout)
    }

    private fun setupActionBar(navController: NavController, appBarConfig: AppBarConfiguration) {
        setupActionBarWithNavController(navController, appBarConfig)
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host).navigateUp(appBarConfiguration)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        for (fragment in nav_host.childFragmentManager.fragments) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }
    
    private fun setToolbar(isVisible: Boolean) {
        if (isVisible) {
            supportActionBar!!.setup(this, backgroundColor = R.color.color_background, icon = "icon")
            activity_main_appbar.outlineProvider = ViewOutlineProvider.BACKGROUND
        } else {
            supportActionBar!!.setup(this, backgroundColor = android.R.color.transparent, icon = "transparent")
            activity_main_appbar.outlineProvider = null
        }
    }

    private fun prepareData() {
        viewModel.syncDataIfNeed().observe(this, Observer {
            when (it) {
                is LoadResult.Loading -> {
                    navController.navigate(R.id.fragment_splash)
                }

                is LoadResult.Success -> {
                    val action = FragmentSplashDirections.actionFragmentSplashToFragmentMain()
                    navController.navigate(action)
                    navController.popBackStack(R.id.fragment_splash, true)
                }

                is LoadResult.Error -> {
                    Snackbar.make(drawer_layout, it.errorMessage.toString(), Snackbar.LENGTH_INDEFINITE).show()
                }
            }
        })
    }

    companion object {
        lateinit var enter: MenuItem
        lateinit var favorites: MenuItem
        lateinit var logout: MenuItem
    }
}


