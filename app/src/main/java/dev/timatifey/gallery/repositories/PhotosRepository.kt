package dev.timatifey.gallery.repositories

import dev.timatifey.gallery.network.Photo

interface IPhotosRepository {
    fun getPhotosByAlbumId(albumId: Int): List<Photo>
    fun savePhotosByAlbumId(albumId: Int, list: List<Photo>)
    fun clearPhotos()
}

object PhotosInMemoryRepository: IPhotosRepository {

    private val photosMap: MutableMap<Int, List<Photo>> = mutableMapOf()

    override fun getPhotosByAlbumId(albumId: Int): List<Photo> {
        return photosMap[albumId] ?: emptyList()
    }

    override fun savePhotosByAlbumId(albumId: Int, list: List<Photo>) {
        photosMap[albumId] = (photosMap[albumId] ?: emptyList()) + list
    }

    override fun clearPhotos() {
        photosMap.clear()
    }
}