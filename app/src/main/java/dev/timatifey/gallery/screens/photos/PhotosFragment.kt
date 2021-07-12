package dev.timatifey.gallery.screens.photos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import dev.timatifey.gallery.R
import dev.timatifey.gallery.screens.common.base.BaseFragment

class PhotosFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_photos, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = arguments?.getInt(EXTRA_ALBUM_ID)
        val text = view.findViewById<TextView>(R.id.text)
        text.text = "Photo with id = $id"
        toolbarController.apply {
            setTitle("Photos")
            setupBack("Users", true)
        }
    }

    companion object {
        const val TAG = "PhotosFragment"
        const val EXTRA_ALBUM_ID = "album_id"

        fun newInstance(albumId: Int): Fragment {
            val photosFragment = PhotosFragment()
            val args = Bundle()
            args.putInt(EXTRA_ALBUM_ID, albumId)
            photosFragment.arguments = args
            return photosFragment
        }
    }
}