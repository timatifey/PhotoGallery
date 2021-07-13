package dev.timatifey.gallery.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.timatifey.gallery.interactors.PhotosInteractor
import dev.timatifey.gallery.network.Photo
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class PhotosViewModel(
    private val photosInteractor: PhotosInteractor,
) : ViewModel() {
    val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Loading)
    val state: StateFlow<State> = _state.asStateFlow()

    sealed class State {
        data class Success(val photos: List<Photo>) : State()
        data class Error(val errorMessageId: Int) : State()
        object Loading : State()
    }

    fun onDestroy() {
        coroutineScope.coroutineContext.cancelChildren()
    }

    fun fetchPhotos(userId: Int) {
        coroutineScope.launch {
            photosInteractor.getPhotosByUserId(userId)
                .flowOn(Dispatchers.Main)
                .collect {
                    _state.value = State.Success(it)
                }
        }
    }
}

class PhotosViewModelFactory(
    private val photosInteractor: PhotosInteractor
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(PhotosInteractor::class.java)
            .newInstance(photosInteractor)
    }

}