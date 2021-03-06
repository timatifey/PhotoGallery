package dev.timatifey.gallery.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.timatifey.gallery.network.User
import dev.timatifey.gallery.interactors.UsersInteractor
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class UsersViewModel(
    private val usersInteractor: UsersInteractor
) : ViewModel() {
    val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Loading)
    val state: StateFlow<State> = _state.asStateFlow()

    sealed class State {
        data class Success(val users: List<User>) : State()
        data class Error(val errorMessageId: Int) : State()
        object Loading : State()
    }

    fun onDestroy() {
        coroutineScope.coroutineContext.cancelChildren()
    }

    fun fetchUsers() {
        coroutineScope.launch {
            usersInteractor.getUsers()
                .flowOn(Dispatchers.Main)
                .collect {
                    _state.value = State.Success(it)
                }

        }
    }
}

class UsersViewModelFactory(private val usersInteractor: UsersInteractor) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(UsersInteractor::class.java).newInstance(usersInteractor)
    }

}