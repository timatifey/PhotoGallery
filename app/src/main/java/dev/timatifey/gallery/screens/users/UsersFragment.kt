package dev.timatifey.gallery.screens.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dev.timatifey.gallery.R
import dev.timatifey.gallery.network.User
import dev.timatifey.gallery.interactors.UsersInteractor
import dev.timatifey.gallery.repositories.UsersInMemoryRepository
import dev.timatifey.gallery.screens.common.MainActivity
import dev.timatifey.gallery.screens.common.base.BaseFragment
import dev.timatifey.gallery.viewmodels.UsersViewModel
import dev.timatifey.gallery.viewmodels.UsersViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class UsersFragment : BaseFragment(), UsersAdapter.Listener {

    private val usersViewModelFactory =
        UsersViewModelFactory(UsersInteractor(UsersInMemoryRepository, apiService))
    private val usersViewModel: UsersViewModel =
        usersViewModelFactory.create(UsersViewModel::class.java)

    private lateinit var recyclerView: RecyclerView
    private lateinit var usersAdapter: UsersAdapter
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_users, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        usersAdapter = UsersAdapter(this)
        recyclerView = view.findViewById(R.id.fragment_users__recyclerView)
        recyclerView.apply {
            adapter = usersAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

            val dividerItemDecoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
            addItemDecoration(dividerItemDecoration)
        }
        progressBar = view.findViewById(R.id.fragment_users__progress_bar)

        usersViewModel.state.onEach {
            when (it) {
                is UsersViewModel.State.Loading -> {
                    progressBar.isVisible = true
                    recyclerView.isVisible = false
                }
                is UsersViewModel.State.Error -> {
                    progressBar.isVisible = false
                    recyclerView.isVisible = true
                    Snackbar.make(
                        requireView(),
                        getString(it.errorMessageId),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                is UsersViewModel.State.Success -> {
                    progressBar.isVisible = false
                    recyclerView.isVisible = true
                    usersAdapter.bindData(it.users)
                    usersAdapter.notifyDataSetChanged()
                }
            }
        }.launchIn(usersViewModel.coroutineScope)

        toolbarController.apply {
            setTitle("Users")
            setupBack("", false)
        }

        usersViewModel.fetchUsers()
    }

    override fun onDestroy() {
        super.onDestroy()
        usersViewModel.onDestroy()
    }

    companion object {
        const val TAG = "UsersFragment"
        fun newInstance(): Fragment = UsersFragment()
    }

    override fun onUserItemClicked(user: User) {
        if (hasConnection()) {
            appRouter.toUserPhotosFragment(user.id)
        } else {
            (activity as MainActivity).tryInit(Bundle())
        }
    }

}