package dev.timatifey.gallery.repositories

import dev.timatifey.gallery.network.User

interface IUsersRepository {
    fun getUsers(): List<User>
    fun saveUsers(users: List<User>)
    fun clearUsers()
}

object UsersInMemoryRepository : IUsersRepository {

    private val users: MutableList<User> = mutableListOf()

    override fun getUsers(): List<User> {
        return users
    }

    override fun saveUsers(users: List<User>) {
        this.users.addAll(users)
    }

    override fun clearUsers() {
        users.clear()
    }

}