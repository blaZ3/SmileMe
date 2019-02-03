package me.tellvivk.smileme.dataSources

import android.content.Context
import io.reactivex.Single
import me.tellvivk.smileme.app.db.ImageDao
import me.tellvivk.smileme.app.model.Image

class LocalImageDataSource(private val context: Context,
                           private val imageDao: ImageDao): ImageDataSourceI {

    override fun loadImages(): Single<DataResponse> {
        return Single.create {
            it.onSuccess(DataResponse(
                success = true,
                items = imageDao.getAll()))
        }
    }

    override fun saveImage(image: Image): Single<Image> {
        return Single.create {
            imageDao.insertAll(image)
            it.onSuccess(image)
        }
    }

}