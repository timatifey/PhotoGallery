package dev.timatifey.gallery.interactors

import dev.timatifey.gallery.network.IApiService
import dev.timatifey.gallery.network.Photo
import dev.timatifey.gallery.repositories.IPhotosRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PhotosInteractor(
    private val photosRepository: IPhotosRepository,
    private val apiService: IApiService,
) {

    private suspend fun getPhotosByAlbumId(albumId: Int): List<Photo> {
        val cache = photosRepository.getPhotosByAlbumId(albumId)
        if (cache.isEmpty()) {
            val newPhotos = apiService.getPhotosByAlbumId(albumId)
            photosRepository.savePhotosByAlbumId(albumId, newPhotos)
            return newPhotos
        }
        return cache
    }

    suspend fun getPhotosByUserId(userId: Int): Flow<List<Photo>> =
        flow {
            emit(apiService.getAlbumsByUserId(userId)
                .map { getPhotosByAlbumId(it) }
                .reduce { a, b -> a + b }
            )
            return@flow
        }

}