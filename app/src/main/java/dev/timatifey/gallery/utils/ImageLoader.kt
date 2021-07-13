package dev.timatifey.gallery.utils

import android.animation.ValueAnimator
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.view.isVisible
import dev.timatifey.gallery.R
import dev.timatifey.gallery.network.Photo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*
import java.lang.ref.SoftReference
import java.net.URL
import java.util.concurrent.ConcurrentHashMap


object ImageLoader {
    const val TAG = "ImageLoader"
    private const val INTERNET_CLIENT =
        "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2"
    private const val USER_AGENT = "User-Agent"

    private val cachedImages: ConcurrentHashMap<Int, SoftReference<Bitmap>> = ConcurrentHashMap()

    private suspend fun downloadImage(
        imageUrl: String,
        progressBar: ProgressBar
    ): Bitmap? = withContext(Dispatchers.IO) {
        try {
            val connection = URL(imageUrl).openConnection()
            connection.setRequestProperty(USER_AGENT, INTERNET_CLIENT)

            val sizeOfImage = connection.contentLength
            val input: InputStream = BufferedInputStream(connection.getInputStream(), 8192)

            val output = ByteArrayOutputStream()
            val data = ByteArray(1024)
            var total = 0L
            progressBar.progress = 0

            var count = input.read(data)
            while (count != -1) {
                total += count
                progressBar.progress = ((total * 100) / sizeOfImage).toInt()
                output.write(data, 0, count)
                count = input.read(data)
            }

            output.flush()

            val bitmap = BitmapFactory.decodeByteArray(
                output.toByteArray(),
                0,
                sizeOfImage
            )
            output.close()
            input.close()
            bitmap
        } catch (e: Throwable) {
            null
        }
    }


    suspend fun load(photo: Photo, imageView: ImageView, progressBar: ProgressBar) {
        withContext(Dispatchers.Main) {
            val fadeIn: Animation = AnimationUtils.loadAnimation(imageView.context, R.anim.fade_in)

            if (cachedImages.containsKey(photo.id)) {
                val cacheImage = cachedImages[photo.id]?.get()
                if (cacheImage != null) {
                    val anim = ValueAnimator.ofInt(imageView.measuredHeight, -100)
                    anim.addUpdateListener { valueAnimator ->
                        val `val` = valueAnimator.animatedValue as Int
                        val layoutParams: ViewGroup.LayoutParams =
                            imageView.layoutParams
                        layoutParams.height = `val`
                        imageView.layoutParams = layoutParams
                    }
                    anim.duration = 500
                    anim.start()

                    imageView.setImageBitmap(cacheImage)
                    progressBar.isVisible = false
                    imageView.visibility = View.VISIBLE
                    imageView.startAnimation(fadeIn)
                    return@withContext
                } else {
                    cachedImages.remove(photo.id)
                }
            }

            imageView.visibility = View.GONE
            progressBar.visibility = View.GONE

            downloadImage(photo.thumbnailUrl, progressBar)?.let {

                imageView.setImageBitmap(it)

                val anim = ValueAnimator.ofInt(imageView.measuredHeight, -100)
                anim.addUpdateListener { valueAnimator ->
                    val `val` = valueAnimator.animatedValue as Int
                    val layoutParams: ViewGroup.LayoutParams =
                        imageView.layoutParams
                    layoutParams.height = `val`
                    imageView.layoutParams = layoutParams
                }
                anim.duration = 500
                anim.start()

                imageView.visibility = View.VISIBLE
                imageView.startAnimation(fadeIn)

                cachedImages[photo.id] = SoftReference(it)
            }

            progressBar.isVisible = true
            downloadImage(photo.url, progressBar)?.let {
                imageView.setImageBitmap(it)
                cachedImages[photo.id] = SoftReference(it)
            }
            progressBar.isVisible = false
        }
    }
}