package me.tellvivk.smileme.helpers.imageHelper

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import java.io.File

class ImageHelper: ImageHelperI {

    private val thumbNailScale = 0.2f

    override fun loadFromUrl(context: Context, url: String, iv: ImageView) {
        Glide.with(context)
            .load(url)
            .thumbnail(thumbNailScale)
            .into(iv)
    }

    override fun loadFromFile(context: Context, file: File, iv: ImageView) {
        Glide.with(context)
            .load(Uri.fromFile(file))
            .thumbnail(thumbNailScale)
            .into(iv)
    }
}