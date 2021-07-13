package dev.timatifey.gallery.screens.common

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import dev.timatifey.gallery.R
import dev.timatifey.gallery.screens.photos.PhotosFragment
import dev.timatifey.gallery.screens.users.UsersFragment

interface IAppRouter {
    fun initialize(outState: Bundle?)
    fun toUserPhotosFragment(userId: Int)
}

class AppRouter(private val fragmentManager: FragmentManager) : IAppRouter {

    override fun initialize(outState: Bundle?) {
        if (outState == null) {
            fragmentManager.beginTransaction().apply {
                add(R.id.activity_main__container, UsersFragment.newInstance())
                commit()
            }
        }
    }

    override fun toUserPhotosFragment(userId: Int) {
        fragmentManager.beginTransaction()
            .replace(R.id.activity_main__container, PhotosFragment.newInstance(userId))
            .addToBackStack(null)
            .commit()
    }

}