package me.tellvivk.smileme.app.model

import io.reactivex.Single

interface ImageRepositoryI {

    fun getImages(screenSize: Pair<Int, Int>): Single<List<Image>>
    fun saveImage(image: Image): Single<Image>

}