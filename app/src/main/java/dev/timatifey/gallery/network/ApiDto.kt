package dev.timatifey.gallery.network

data class User(
    val id: Int,
    val name: String,
)

data class Photo(
    val albumId: Int,
    val id: Int,
    val title: String,
    val url: String,
    val thumbnailUrl: String,
)