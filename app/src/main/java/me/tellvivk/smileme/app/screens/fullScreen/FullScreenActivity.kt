package me.tellvivk.smileme.app.screens.fullScreen

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.autoDisposable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_full_screen.*
import me.tellvivk.smileme.R
import me.tellvivk.smileme.app.base.BaseActivity
import me.tellvivk.smileme.app.base.BaseView
import me.tellvivk.smileme.app.base.StateModel
import me.tellvivk.smileme.app.base.ViewEvent
import me.tellvivk.smileme.app.model.Image
import me.tellvivk.smileme.helpers.imageHelper.ImageHelperI
import org.koin.android.ext.android.get
import shareViaIntent
import java.io.File


class FullScreenActivity : BaseActivity(), BaseView {
    private lateinit var viewModel: FullScreenImageViewModel
    private lateinit var imageHelper: ImageHelperI
    private var image: Image? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen)
        imageHelper = get()
        intent.extras?.let {
            if (it.containsKey(EXTRA_IMAGE)) {
                image = it.getParcelable(EXTRA_IMAGE)
            }
        }
        image?.title?.let {
            initToolbar(it, toolbarFull)
        }
    }

    override fun onStart() {
        super.onStart()
        initView()
    }

    override fun initView() {
        viewModel = get()

        viewModel.getViewModelObservable()
            .autoDisposable(AndroidLifecycleScopeProvider.from(this))
            .subscribe { updateView(it) }

        viewModel.getViewEventObservable()
            .autoDisposable(AndroidLifecycleScopeProvider.from(this))
            .subscribe { handleEvent(it) }

        image?.let { img->
            if (!img.imgUrl.isNullOrEmpty()){
                imageHelper.loadFromUrl(context = this,
                    url = img.imgUrl!!, iv = imgFullScreenImage)
            } else if(!img.filePath.isNullOrEmpty()){
                imageHelper.loadFromFile(context = this,
                    file = File(img.filePath),
                    iv = imgFullScreenImage)
            }

            img.comment.let { desc->
                txtFullScreenImageDescription.text = desc
            }

            fabSharePic.setOnClickListener {
                if (!img.imgUrl.isNullOrEmpty()){
                    Single.create<String> { emitter->
                        val file: File = Glide.with(this)
                            .asFile()
                            .load(img.imgUrl)
                            .submit()
                            .get()
                        val path: String = file.path
                        emitter.onSuccess(path)
                    }.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSuccess { downloadedFilePath->
                            downloadedFilePath.shareViaIntent(this)
                        }
                        .subscribe()


                } else if(!img.filePath.isNullOrEmpty()){
                    img.filePath!!.shareViaIntent(this)
                }

            }
        }
    }

    override fun updateView(stateModel: StateModel) {
        (stateModel as FullScreenStateModel).apply {}
    }

    override fun handleEvent(event: ViewEvent) {
        (event as FullScreenViewEvent).apply {}
    }

    override fun getParentView(): BaseView? {
        return null
    }

    companion object {
        const val EXTRA_IMAGE = "EXTRA_IMAGE"
        fun start(activity: BaseActivity, image: Image) {
            val intent = Intent(activity, FullScreenActivity::class.java)
            intent.putExtra(EXTRA_IMAGE, image)
            activity.startActivity(intent)
        }
    }
}

