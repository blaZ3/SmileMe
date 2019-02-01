package me.tellvivk.smileme.dataSources

import io.reactivex.Single
import me.tellvivk.smileme.helpers.TestHelper

class DummyImageDataSource : ImageDataSourceI {


    override fun loadImages(): Single<DataResponse> {
        return Single.just(
            DataResponse(
                success = true,
                items = TestHelper.getDummyImages()
            )
        )
    }
}