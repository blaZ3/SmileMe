package me.tellvivk.smileme.app.screens.fullScreen

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.autoDisposable
import kotlinx.android.synthetic.main.activity_full_screen.*
import me.tellvivk.smileme.R
import me.tellvivk.smileme.app.base.BaseActivity
import me.tellvivk.smileme.app.base.BaseView
import me.tellvivk.smileme.app.base.StateModel
import me.tellvivk.smileme.app.base.ViewEvent
import me.tellvivk.smileme.app.model.Image
import me.tellvivk.smileme.helpers.imageHelper.ImageHelperI
import org.koin.android.ext.android.get
import java.io.File

class FullScreenActivity : AppCompatActivity(), BaseView {

    private var image: Image? = null
    private lateinit var viewModel: FullScreenImageViewModel

    private lateinit var imageHelper: ImageHelperI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen)

        imageHelper = get()

        val b = intent.extras
        if (b.containsKey(EXTRA_IMAGE)) {
            image = b.getParcelable(EXTRA_IMAGE)
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

        image?.let {
            if (!it.imgUrl.isNullOrEmpty()){
                imageHelper.loadFromUrl(context = this,
                    url = it.imgUrl!!, iv = imgFullScreenImage)
            } else if(!it.filePath.isNullOrEmpty()){
                imageHelper.loadFromFile(context = this,
                    file = File(it.filePath),
                    iv = imgFullScreenImage)
            }

            it.title.apply {
                txtFullScreenImageDescription.text = this
            }

            it.comment.apply {
                txtFullScreenImageDescription.text =
                        "${txtFullScreenImageDescription.text.toString()}\n$this"
            }

            fabSharePic.setOnClickListener {

            }
        }
    }

    override fun getParentView(): BaseView? {
        return null
    }

    override fun updateView(stateModel: StateModel) {
        (stateModel as FullScreenStateModel).apply {

        }
    }

    override fun handleEvent(event: ViewEvent) {
        (event as FullScreenViewEvent).apply {

        }
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
