package me.tellvivk.smileme.app.screens.home

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.tellvivk.smileme.R
import me.tellvivk.smileme.app.base.BaseViewModel
import me.tellvivk.smileme.app.base.ProgressStateModel
import me.tellvivk.smileme.app.base.StateModel
import me.tellvivk.smileme.app.base.ViewEvent
import me.tellvivk.smileme.app.model.Image
import me.tellvivk.smileme.app.model.ImageRepositoryI
import me.tellvivk.smileme.helpers.stringFetcher.StringFetcherI

class HomeViewModel(private val imagesRepo: ImageRepositoryI,
                    private val stringFetcher: StringFetcherI): BaseViewModel() {

    init {
        model = HomeStateModel()
        initEvent = InitHomeEvent
    }

    fun gotImage(imagePath: String, screenW: Int, screenH: Int){
        (model as HomeStateModel).apply {

            val bmOptions = BitmapFactory.Options().apply {
                // Get the dimensions of the bitmap
                inJustDecodeBounds = true
                BitmapFactory.decodeFile(imagePath, this)
                val photoW: Int = outWidth
                val photoH: Int = outHeight

                // Determine how much to scale down the image
                val scaleFactor: Int = Math.min(photoW / screenW, photoH / screenH)

                // Decode the image file into a Bitmap sized to fill the View
                inJustDecodeBounds = false
                inSampleSize = scaleFactor
                inPurgeable = true
            }
            BitmapFactory.decodeFile(imagePath, bmOptions)?.also { bitmap ->
                updateModel(this.copy(selectedImagePath = imagePath, selectedImageThumbNail = bitmap))
            }
        }
    }

    fun getImages(){
        (model as HomeStateModel).apply {
            updateModel(this.copy(progress = this.progress.copy(isShown = true,
                text = stringFetcher.getString(R.string.str_progress))))
        }
        imagesRepo
            .getImages()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                (model as HomeStateModel).apply {
                    updateModel(this.copy(
                        images = it,
                        progress = this.progress.copy(isShown = false)))
                }
            }.doOnError {
                sendEvent(LoadingErrorEvent(it.message.toString()))
            }.subscribe()
    }

}


data class HomeStateModel(
    val images: List<Image> = listOf(),
    val progress:ProgressStateModel =  ProgressStateModel(),

    val selectedImagePath: String = "",
    val selectedImageThumbNail: Bitmap? = null
): StateModel()


sealed class HomeViewEvent: ViewEvent
object InitHomeEvent: HomeViewEvent()
data class LoadingErrorEvent(val msg: String): HomeViewEvent()
data class ShowImageDescriptionDialog(val selectedImageThumbNail: Bitmap): HomeViewEvent()