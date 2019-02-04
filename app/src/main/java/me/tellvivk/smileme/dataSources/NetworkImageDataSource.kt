package me.tellvivk.smileme.dataSources

import io.reactivex.Single
import me.tellvivk.smileme.app.model.Image
import me.tellvivk.smileme.helpers.networkHelper.NetworkHelper
import me.tellvivk.smileme.helpers.networkHelper.NetworkHelperI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class NetworkImageDataSource(retrofit: Retrofit,
                             private val networkHelper: NetworkHelperI) : ImageDataSourceI {

    private val imageService = retrofit.create(ImageService::class.java)

    override fun loadImages(): Single<DataResponse> {
        return Single.create { emitter ->
            if (networkHelper.isConncected()){
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
            }else {
                emitter.onSuccess(
                    DataResponse(
                    success = false,
                    items = listOf<Image>()
                ))
            }
        }
    }

    override fun saveImage(image: Image): Single<Image> {
        return Single.never()
    }

}