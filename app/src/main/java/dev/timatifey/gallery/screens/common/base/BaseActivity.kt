package dev.timatifey.gallery.screens.common.base

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import dev.timatifey.gallery.screens.common.AppRouter
import dev.timatifey.gallery.screens.common.IAppRouter

abstract class BaseActivity : AppCompatActivity() {

    protected val appRouter: IAppRouter by lazy {
        AppRouter(supportFragmentManager)
    }

    fun hasConnection(): Boolean {
        val connectivityManager =
            applicationContext.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            } else {
                return true
            }
        return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
    }

}