package me.tellvivk.smileme.dataSources

import io.reactivex.Single
import me.tellvivk.smileme.app.model.Image
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class NetworkImageDataSource(retrofit: Retrofit) : ImageDataSourceI {

    private val imageService = retrofit.create(ImageService::class.java)

    override fun loadImages(): Single<DataResponse> {
        return Single.create { emitter ->
            imageService.getImages().enqueue(object : Callback<List<Image>> {
                override fun onResponse(call: Call<List<Image>>, response: Response<List<Image>>) {
                    if (response.isSuccessful){
                        emitter.onSuccess(
                            DataResponse(
                            success = true,
                            items = response.body()
                        ))
                    }else{
                        emitter.onSuccess(DataResponse(
                            success = false
                        ))
                    }
                }

                override fun onFailure(call: Call<List<Image>>, t: Throwable) {
                    emitter.onError(t)
                }
            })
        }
    }

    override fun saveImage(image: Image): Single<Image> {
        return Single.never()
    }

}