package me.tellvivk.smileme.app.screens.home

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.tellvivk.smileme.app.base.BaseViewModel
import me.tellvivk.smileme.app.base.ProgressStateModel
import me.tellvivk.smileme.app.base.StateModel
import me.tellvivk.smileme.app.base.ViewEvent
import me.tellvivk.smileme.app.model.Image
import me.tellvivk.smileme.app.model.ImageRepositoryI

class HomeViewModel(private val imagesRepo: ImageRepositoryI): BaseViewModel() {

    init {
        model = HomeStateModel()
        initEvent = InitHomeEvent
    }

    fun getImages(){
        imagesRepo
            .getImages()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {

                (model as HomeStateModel).apply {
                    updateModel(this.copy(images = it))
                }

            }.subscribe()
    }

}


data class HomeStateModel(
    val images: List<Image> = listOf(),
    val progress:ProgressStateModel =  ProgressStateModel()
): StateModel()


sealed class HomeViewEvent: ViewEvent
object InitHomeEvent: HomeViewEvent()