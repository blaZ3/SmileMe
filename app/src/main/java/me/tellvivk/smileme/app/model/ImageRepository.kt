package me.tellvivk.smileme.app.model

import io.reactivex.Single
import me.tellvivk.smileme.dataSources.ImageDataSourceI

class ImageRepository(private val networkDataSource: ImageDataSourceI): ImageRepositoryI {

    override fun getImages(): Single<List<Image>> {
        return Single.create{ emitter ->
            networkDataSource.loadImages()
                .doOnSuccess {
                    if (it.success) {
                        emitter.onSuccess(it.items as List<Image>)
                    } else {
                        emitter.onSuccess(listOf())
                    }
                }.doOnError {
                    emitter.onError(it)
                }.subscribe()
        }
    }
}