package dev.timatifey.gallery.screens.photos

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dev.timatifey.gallery.R
import dev.timatifey.gallery.interactors.PhotosInteractor
import dev.timatifey.gallery.repositories.PhotosInMemoryRepository
import dev.timatifey.gallery.screens.common.base.BaseFragment
import dev.timatifey.gallery.viewmodels.PhotosViewModel
import dev.timatifey.gallery.viewmodels.PhotosViewModelFactory
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class PhotosFragment : BaseFragment() {

    private lateinit var photosViewModel: PhotosViewModel

    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var photosAdapter: PhotosAdapter

    private var id: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_photos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = view.findViewById(R.id.fragment_photos__progress)
        photosAdapter = PhotosAdapter()
        recyclerView = view.findViewById(R.id.fragment_photos__recyclerView)
        recyclerView.apply {
            adapter = photosAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        photosViewModel = PhotosViewModelFactory(
            PhotosInteractor(PhotosInMemoryRepository, apiService)
        ).create(PhotosViewModel::class.java)

        photosViewModel.state.onEach {
            when (it) {
                is PhotosViewModel.State.Loading -> {
                    progressBar.isVisible = true
                    recyclerView.isVisible = false
                }
                is PhotosViewModel.State.Error -> {
                    progressBar.isVisible = false
                    recyclerView.isVisible = true
                    Snackbar.make(
                        requireView(),
                        getString(it.errorMessageId),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                is PhotosViewModel.State.Success -> {
                    progressBar.isVisible = false
                    recyclerView.isVisible = true
                    photosAdapter.bindData(it.photos)
                    photosAdapter.notifyDataSetChanged()
                }
            }
        }.launchIn(photosViewModel.coroutineScope)
        id = arguments?.getInt(EXTRA_USER_ID)
        photosViewModel.fetchPhotos(id!!)

        toolbarController.apply {
            setTitle("Photos")
            setupBack("Users", true)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        id = null
        photosViewModel.onDestroy()
    }

    companion object {
        const val TAG = "PhotosFragment"
        const val EXTRA_USER_ID = "user_id"

        fun newInstance(userId: Int): Fragment {
            val photosFragment = PhotosFragment()
            val args = Bundle()
            args.putInt(EXTRA_USER_ID, userId)
            photosFragment.arguments = args
            return photosFragment
        }
    }
}