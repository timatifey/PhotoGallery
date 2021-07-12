package dev.timatifey.gallery.screens.common.base

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

}