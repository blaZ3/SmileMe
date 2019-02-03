package me.tellvivk.smileme.dataSources

import io.reactivex.Single
import me.tellvivk.smileme.app.model.Image

interface ImageDataSourceI {

    fun loadImages(): Single<DataResponse>
    fun saveImage(image: Image): Single<Image>

}