package me.tellvivk.smileme.dataSources

import io.reactivex.Single
import me.tellvivk.smileme.app.model.Image
import me.tellvivk.smileme.helpers.TestHelper

class DummyImageDataSource : ImageDataSourceI {

    override fun saveImage(image: Image): Single<Image> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadImages(): Single<DataResponse> {
        return Single.just(
            DataResponse(
                success = true,
                items = TestHelper.getDummyImages()
            )
        )
    }
}