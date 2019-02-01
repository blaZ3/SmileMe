package me.tellvivk.smileme.dataSources

import me.tellvivk.smileme.app.model.Image
import retrofit2.Call
import retrofit2.http.GET

interface ImageService {

    @GET("get/cftPFNNHsi")
    fun getImages(): Call<List<Image>>

}