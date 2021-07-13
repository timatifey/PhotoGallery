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
    companion object {
        const val USERS_TAG = "UsersFragmentTAG"
        const val PHOTOS_TAG = "PhotosFragmentTAG"
    }

    override fun initialize(outState: Bundle?) {
        if (outState == null) {
            fragmentManager.beginTransaction().apply {
                add(R.id.activity_main__container, UsersFragment.newInstance(), USERS_TAG)
                commit()
            }
        }
    }

    override fun toUserPhotosFragment(userId: Int) {
        fragmentManager.beginTransaction()
            .replace(R.id.activity_main__container, PhotosFragment.newInstance(userId), PHOTOS_TAG)
            .addToBackStack(null)
            .commit()
    }
}