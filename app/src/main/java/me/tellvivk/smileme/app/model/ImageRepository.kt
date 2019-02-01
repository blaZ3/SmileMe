package me.tellvivk.smileme.app.model

import io.reactivex.Single
import me.tellvivk.smileme.dataSources.ImageDataSourceI

class ImageRepository(private val networkDataSource: ImageDataSourceI): ImageRepositoryI {

    override fun getImages(): Single<List<Image>> {
        return networkDataSource.loadImages().map {
            if (it.success) {
                return@map it.items as List<Image>
            } else {
                return@map listOf<Image>()
            }
        }
    }
}