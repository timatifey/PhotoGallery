package dev.timatifey.gallery.screens.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.timatifey.gallery.R
import dev.timatifey.gallery.data.User
import dev.timatifey.gallery.screens.common.base.BaseFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random

class UsersFragment : BaseFragment(), UsersAdapter.Listener {

    lateinit var recyclerView: RecyclerView
    lateinit var usersAdapter: UsersAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_users, container, false)
        return view
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

        CoroutineScope(Dispatchers.Main).launch {
            usersAdapter.addAll(apiService.getUsers())
        }

        toolbarController.apply {
            setTitle("Users")
            setupBack("", false)
        }
    }

    companion object {
        const val TAG = "UsersFragment"
        fun newInstance(): Fragment = UsersFragment()
    }

    override fun onUserItemClicked(user: User) {
        appRouter.toUserPhotosFragment(user.id)
    }

}

fun generateUsers(): List<User> {
    val l = mutableListOf<User>()
    for (i in 1..20) {
        l.add(
            User(
                i, randomWord(30)
            )
        )
    }
    return l
}

private fun randomWord(wordLength: Long): String {
    val source = "abcdefghijklmnopqrstuvwxyz"
    val list = mutableListOf<Int>()
    for (i in 0 until wordLength) {
        list.add(Random.nextInt(0, source.length))
    }
    return list.asSequence()
        .map(source::get)
        .joinToString("")
}