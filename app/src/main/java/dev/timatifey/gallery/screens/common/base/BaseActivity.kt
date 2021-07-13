package dev.timatifey.gallery.screens.common.base

import androidx.appcompat.app.AppCompatActivity
import dev.timatifey.gallery.screens.common.AppRouter
import dev.timatifey.gallery.screens.common.IAppRouter

abstract class BaseActivity : AppCompatActivity() {

    protected val appRouter: IAppRouter by lazy {
        AppRouter(supportFragmentManager)
    }

}