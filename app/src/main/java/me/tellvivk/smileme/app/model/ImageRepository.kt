package me.tellvivk.smileme.app.model

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import me.tellvivk.smileme.dataSources.ImageDataSourceI
import java.util.*

class ImageRepository(
    private val networkDataSource: ImageDataSourceI,
    private val localDataSource: ImageDataSourceI
) : ImageRepositoryI {

    override fun getImages(): Single<List<Image>> {

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
            }.flatMapIterable { it }.map {
                val url = "${it.imgUrl}&cacheBust=${UUID.randomUUID().hashCode()}"
                return@map it.copy(imgUrl = url)
            }

        return Single.create { emitter ->
            Observable.merge(
                localImageObservable.subscribeOn(Schedulers.io()),
                networkImageObservable.subscribeOn(Schedulers.io())
            ).toList().doOnSuccess {
                emitter.onSuccess(it)
            }.doOnError {
                emitter.onError(it)
            }.subscribe()
        }
    }

    override fun saveImage(image: Image): Single<Image> {
        return localDataSource.saveImage(image)
    }
}