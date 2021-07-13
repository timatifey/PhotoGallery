package dev.timatifey.gallery.screens.common.base

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import dev.timatifey.gallery.network.ApiService
import dev.timatifey.gallery.network.IApiService
import dev.timatifey.gallery.screens.common.AppRouter
import dev.timatifey.gallery.screens.common.IAppRouter
import dev.timatifey.gallery.screens.common.ToolbarController
import kotlinx.coroutines.Dispatchers

abstract class BaseFragment : Fragment() {

    protected val appRouter: IAppRouter by lazy { AppRouter(parentFragmentManager) }

    protected val toolbarController: ToolbarController
        get() = activity as ToolbarController

    protected val apiService: IApiService by lazy {
        ApiService.getInstance(Dispatchers.IO)
    }

    fun hasConnection(): Boolean {
        val connectivityManager =
            requireContext().getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            } else {
                return true
            }
        return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
    }

}