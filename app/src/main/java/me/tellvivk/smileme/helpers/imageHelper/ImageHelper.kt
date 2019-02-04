package me.tellvivk.smileme.helpers.imageHelper

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import java.io.File
import android.annotation.SuppressLint
import com.bumptech.glide.request.RequestOptions



class ImageHelper: ImageHelperI {
    private val thumbNailScale = 0.5f
    private val fileThumbNailScale = 0.2f

    @SuppressLint("CheckResult")
    override fun loadFromUrl(context: Context, url: String, iv: ImageView) {
        val requestOptions = RequestOptions()
        requestOptions.placeholder(me.tellvivk.smileme.R.drawable.place_holder)
        requestOptions.error(me.tellvivk.smileme.R.drawable.place_holder)

        Glide.with(context)
            .setDefaultRequestOptions(requestOptions)
            .load(url)
            .thumbnail(thumbNailScale)
            .into(iv)
    }

    @SuppressLint("CheckResult")
    override fun loadFromFile(context: Context, file: File, iv: ImageView) {
        val requestOptions = RequestOptions()
        requestOptions.placeholder(me.tellvivk.smileme.R.drawable.place_holder)
        requestOptions.error(me.tellvivk.smileme.R.drawable.place_holder)

        Glide.with(context)
            .setDefaultRequestOptions(requestOptions)
            .load(Uri.fromFile(file))
            .thumbnail(fileThumbNailScale)
            .into(iv)
    }
}