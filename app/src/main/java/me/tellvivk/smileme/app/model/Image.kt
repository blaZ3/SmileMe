package me.tellvivk.smileme.app.model

import android.graphics.Bitmap
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Image(
    @SerializedName("_id")
    @Expose
    val id: String? = "",

    @SerializedName("picture")
    @Expose
    val imgUrl: String? = "",

    @Expose
    val comment: String? = "",

    @Expose
    val title: String? = "",

    @Expose
    val publishedAt: String? = "",

    val bitmap: Bitmap? = null
)