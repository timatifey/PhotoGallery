package dev.timatifey.gallery.network

import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import dev.timatifey.gallery.utils.SingletonHolder
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL
import kotlin.coroutines.CoroutineContext

interface IApiService {
    suspend fun getUsers(): List<User>
    suspend fun getAlbumsByUserId(userId: Int): List<Int>
    suspend fun getPhotosByAlbumId(albumId: Int): List<Photo>
}

class ApiService(
    private val coroutineContext: CoroutineContext
) : IApiService {

    private suspend fun <T> downloadAndParse(url: String, parse: (JSONObject) -> T): List<T> =
        withContext(coroutineContext) {
            val result = URL(url).readText()
            val jsonArray = JSONArray(result)
            val list = mutableListOf<T>()
            var index = 0
            while (jsonArray.length() != index) {
                val current = JSONObject(jsonArray.get(index++).toString())
                list.add(parse(current))
            }
            return@withContext list
        }

    override suspend fun getUsers(): List<User> =
        downloadAndParse("$BASE_URL/users") { json ->
            User(
                json.getInt("id"),
                json.getString("name")
            )
        }

    override suspend fun getAlbumsByUserId(userId: Int): List<Int> =
        downloadAndParse("$BASE_URL/albums?userId=$userId") { json ->
            json.getInt("id")
        }

    override suspend fun getPhotosByAlbumId(albumId: Int): List<Photo> =
        downloadAndParse("$BASE_URL/photos?albumId=$albumId") { json ->
            Photo(
                json.getInt("albumId"),
                json.getInt("id"),
                json.getString("title"),
                json.getString("url"),
                json.getString("thumbnailUrl")
            )
        }

    companion object: SingletonHolder<IApiService, CoroutineContext>(::ApiService) {
        const val BASE_URL = "https://jsonplaceholder.typicode.com"
    }
}