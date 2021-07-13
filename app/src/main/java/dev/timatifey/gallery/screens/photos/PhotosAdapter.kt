package dev.timatifey.gallery.screens.photos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.timatifey.gallery.R
import dev.timatifey.gallery.network.Photo
import dev.timatifey.gallery.utils.ImageLoader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PhotosAdapter : RecyclerView.Adapter<PhotosAdapter.CardViewHolder>() {

    private var photoList: List<Photo> = emptyList()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.item_photo__imageview)
        val title: TextView = itemView.findViewById(R.id.item_photo__title)
        val progress: ProgressBar = itemView.findViewById(R.id.item_photo__progress)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val photo = photoList[position]
        holder.apply {
            coroutineScope.launch { ImageLoader.load(photo, imageView, progress) }
            title.text = photo.title
        }
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

    fun bindData(photos: List<Photo>) {
        photoList = photos
    }

}