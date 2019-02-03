package me.tellvivk.smileme.app.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
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
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(imgUrl)
        parcel.writeString(comment)
        parcel.writeString(title)
        parcel.writeString(publishedAt)
        parcel.writeString(filePath)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Image> {
        override fun createFromParcel(parcel: Parcel): Image {
            return Image(parcel)
        }

        override fun newArray(size: Int): Array<Image?> {
            return arrayOfNulls(size)
        }
    }
}