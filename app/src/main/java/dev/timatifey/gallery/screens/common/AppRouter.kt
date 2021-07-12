package dev.timatifey.gallery.screens.common

import androidx.fragment.app.FragmentManager
import dev.timatifey.gallery.R
import dev.timatifey.gallery.screens.photos.PhotosFragment
import dev.timatifey.gallery.screens.users.UsersFragment
import dev.timatifey.gallery.utils.SingletonHolder

interface IAppRouter {
    fun initialize()
    fun navigateUp()
    fun toUserPhotosFragment(albumId: Int)
}

class AppRouter(private val fragmentManager: FragmentManager) : IAppRouter {

    override fun initialize() {
        fragmentManager.beginTransaction()
            .replace(R.id.activity_main__container, UsersFragment.newInstance())
            .commit()
    }

    override fun navigateUp() {
        fragmentManager.popBackStack()
    }

    override fun toUserPhotosFragment(albumId: Int) {
        fragmentManager.beginTransaction()
            .replace(R.id.activity_main__container, PhotosFragment.newInstance(albumId))
            .addToBackStack(null)
            .commit()
    }

}