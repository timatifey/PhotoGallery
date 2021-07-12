package dev.timatifey.gallery.screens.common.base

import androidx.appcompat.app.AppCompatActivity
import dev.timatifey.gallery.network.ApiService
import dev.timatifey.gallery.network.IApiService
import dev.timatifey.gallery.screens.common.AppRouter
import dev.timatifey.gallery.screens.common.IAppRouter
import kotlinx.coroutines.Dispatchers

abstract class BaseActivity : AppCompatActivity() {

    protected val appRouter: IAppRouter by lazy {
        AppRouter(supportFragmentManager)
    }

}