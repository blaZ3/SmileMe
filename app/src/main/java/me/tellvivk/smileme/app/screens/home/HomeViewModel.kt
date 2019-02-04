package me.tellvivk.smileme.app.screens.home

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.tellvivk.smileme.R
import me.tellvivk.smileme.app.base.BaseViewModel
import me.tellvivk.smileme.app.base.ProgressStateModel
import me.tellvivk.smileme.app.base.StateModel
import me.tellvivk.smileme.app.base.ViewEvent
import me.tellvivk.smileme.app.model.Image
import me.tellvivk.smileme.app.model.ImageRepositoryI
import me.tellvivk.smileme.helpers.fileHelper.FileHelperI
import me.tellvivk.smileme.helpers.stringFetcher.StringFetcherI
import java.util.*

class HomeViewModel(
    private val imagesRepo: ImageRepositoryI,
    private val stringFetcher: StringFetcherI,
    private val fileHelper: FileHelperI,
    private val screenSize: Pair<Int, Int>
) : BaseViewModel() {

    init {
        model = HomeStateModel()
        initEvent = InitHomeEvent
    }

    fun gotImage(imagePath: String) {
        (model as HomeStateModel).apply {
            updateModel(
                this.copy(
                    selectedImagePath = imagePath
                )
            )
            sendEvent(
                ShowImageDescriptionDialog(
                    selectedImagePath = imagePath
                )
            )
        }
    }

    fun saveImage(title: String, comment: String) {
        (model as HomeStateModel).apply {
            val image = Image(
                filePath = selectedImagePath,
                imgUrl = "",
                title = title,
                comment = comment,
                id = UUID.randomUUID().hashCode().toString(),
                publishedAt = Date().toString()
            )

            sendEvent(ShowBlockingProgress)
            imagesRepo.saveImage(image)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess {
                    val newList = arrayListOf<Image>()
                    newList.addAll(this.images)
                    newList.add(0, image)

                    updateModel(this.copy(images = newList))
                    sendEvent(HideBlockingProgress)
                    sendEvent(ScrollToTop)
                }.subscribe()
        }
    }

    fun getImages() {
        (model as HomeStateModel).apply {
            updateModel(
                this.copy(
                    progress = this.progress.copy(
                        isShown = true,
                        text = stringFetcher.getString(R.string.str_progress)
                    )
                )
            )
        }

        imagesRepo
            .getImages(screenSize)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess { list->
                var gotFromNetwork = false
                (model as HomeStateModel).apply {
                    if (list.isNotEmpty()){
                        for(image in list){
                            if (!image.imgUrl.isNullOrEmpty()){
                                gotFromNetwork = true
                                break
                            }
                        }
                        updateModel(
                            this.copy(
                                images = list,
                                progress = this.progress.copy(isShown = false)
                            )
                        )
                    }
                    if (!gotFromNetwork) {
                        sendEvent(LoadingErrorEvent(
                            stringFetcher.getString(R.string.str_network_error)))
                    }
                }

            }.doOnError {
                sendEvent(LoadingErrorEvent(it.message.toString()))
            }.subscribe()
    }

}


data class HomeStateModel(
    val images: List<Image> = listOf(),
    val progress: ProgressStateModel = ProgressStateModel(),

    val selectedImagePath: String = ""
) : StateModel()


sealed class HomeViewEvent : ViewEvent
object InitHomeEvent : HomeViewEvent()
object ScrollToTop : HomeViewEvent()
object ShowBlockingProgress : HomeViewEvent()
object HideBlockingProgress : HomeViewEvent()
data class LoadingErrorEvent(val msg: String) : HomeViewEvent()
data class ShowImageDescriptionDialog(val selectedImagePath: String) : HomeViewEvent()