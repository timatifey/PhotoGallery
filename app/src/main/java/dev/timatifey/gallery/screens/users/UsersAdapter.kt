package dev.timatifey.gallery.screens.users

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.timatifey.gallery.R
import dev.timatifey.gallery.network.User

class UsersAdapter(private val listener: Listener) :
    RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {

    private var userList: List<User> = emptyList()

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userName: TextView = itemView.findViewById(R.id.item_user__name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        view.setOnClickListener {
            listener.onUserItemClicked(it.tag as User)
        }
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.apply {
            userName.text = user.name
            itemView.tag = user
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun bindData(users: List<User>) {
        userList = users
    }

    interface Listener {
        fun onUserItemClicked(user: User)
    }
}