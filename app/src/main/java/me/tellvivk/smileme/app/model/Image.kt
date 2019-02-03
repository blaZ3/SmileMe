package me.tellvivk.smileme.app.model

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity
data class Image(
    @SerializedName("_id")
    @Expose
    @PrimaryKey
    var id: String = "",

    @SerializedName("picture")
    @Expose
    var imgUrl: String? = "",

    @Expose
    var comment: String? = "",

    @Expose
    var title: String? = "",

    @Expose
    var publishedAt: String? = "",

    var filePath: String? = ""
)