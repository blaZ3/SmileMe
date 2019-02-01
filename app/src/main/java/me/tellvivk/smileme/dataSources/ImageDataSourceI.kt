package me.tellvivk.smileme.dataSources

import io.reactivex.Single

interface ImageDataSourceI {

    fun loadImages(): Single<DataResponse>

}