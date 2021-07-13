package dev.timatifey.gallery.interactors

import dev.timatifey.gallery.network.User
import dev.timatifey.gallery.network.IApiService
import dev.timatifey.gallery.repositories.IUsersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UsersInteractor(
    private val usersRepository: IUsersRepository,
    private val apiService: IApiService,
) {

    suspend fun getUsers(): Flow<List<User>> =
        flow {
            val cacheUsers = usersRepository.getUsers()
            if (cacheUsers.isEmpty()) {
                val newUsers = apiService.getUsers()
                usersRepository.saveUsers(newUsers)
                emit(newUsers)
                return@flow
            }
            emit(cacheUsers)
            return@flow
        }

}