package ru.imlocal

import android.app.Application
import android.content.Context
import android.content.Intent
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKAccessTokenTracker
import com.vk.sdk.VKSdk
import ru.imlocal.ui.MainActivity

class App : Application() {

    init {
        instance = this
    }

    companion object {
        private lateinit var instance: App

        fun applicationContext(): Context = instance.applicationContext
    }

    var vkAccessTokenTracker: VKAccessTokenTracker = object : VKAccessTokenTracker() {
        override fun onVKAccessTokenChanged(oldToken: VKAccessToken?, newToken: VKAccessToken?) {
            if (newToken == null) {
                val intent = Intent(this@App, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        vkAccessTokenTracker.startTracking()
        VKSdk.initialize(this)
    }
}