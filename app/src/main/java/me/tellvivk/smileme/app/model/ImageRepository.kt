package me.tellvivk.smileme.app.model

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import me.tellvivk.smileme.dataSources.ImageDataSourceI
import me.tellvivk.smileme.helpers.fileHelper.FileHelperI

class ImageRepository(
    private val networkDataSource: ImageDataSourceI,
    private val localDataSource: ImageDataSourceI,
    private val fileHelper: FileHelperI
) : ImageRepositoryI {

    override fun getImages(screenSize: Pair<Int, Int>): Single<List<Image>> {

        val localImageObservable = localDataSource.loadImages()
            .toObservable()
            .map { it.items as List<Image> }
            .flatMapIterable { it }

        val networkImageObservable = networkDataSource.loadImages()
            .toObservable()
            .map {
                if (it.success) {
                    return@map it.items as List<Image>
                } else {
                    return@map listOf<Image>()
                }
            }.flatMapIterable { it }

        return Single.create { emitter ->
            Observable.merge(
                localImageObservable.subscribeOn(Schedulers.io()),
                networkImageObservable.subscribeOn(Schedulers.io())
            ).toList().doOnSuccess {
                    emitter.onSuccess(it)
                }.subscribe()
        }
    }

    override fun saveImage(image: Image): Single<Image> {
        return localDataSource.saveImage(image)
    }
}