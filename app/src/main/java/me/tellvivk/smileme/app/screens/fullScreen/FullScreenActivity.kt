package me.tellvivk.smileme.app.screens.fullScreen

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.autoDisposable
import kotlinx.android.synthetic.main.activity_full_screen.*
import me.tellvivk.smileme.R
import me.tellvivk.smileme.app.base.BaseActivity
import me.tellvivk.smileme.app.base.BaseView
import me.tellvivk.smileme.app.base.StateModel
import me.tellvivk.smileme.app.base.ViewEvent
import me.tellvivk.smileme.app.model.Image
import org.koin.android.ext.android.get
import java.lang.Exception
import java.util.*

class FullScreenActivity : AppCompatActivity(), BaseView {

    private lateinit var imgUrl: String

    private lateinit var viewModel: FullScreenImageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen)

        val b = intent.extras
        if (b.containsKey(EXTRA_IMG_URL)){
            imgUrl = b.getString(EXTRA_IMG_URL, "")
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
            .subscribe{ handleEvent(it) }


        progressFullScreenImage.visibility = View.VISIBLE
        imgFullScreenImage.visibility = View.GONE
        Picasso.get()
            .load(imgUrl)
            .placeholder(R.drawable.place_holder)
            .into(imgFullScreenImage, object: Callback{
                override fun onSuccess() {
                    imgFullScreenImage.visibility = View.VISIBLE
                    progressFullScreenImage.visibility = View.GONE
                }

                override fun onError(e: Exception?) {
                    imgFullScreenImage.visibility = View.VISIBLE
                    progressFullScreenImage.visibility = View.GONE
                    imgFullScreenImage.setImageDrawable(
                        resources.getDrawable(R.drawable.place_holder))
                }
            })
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
        const val EXTRA_IMG_URL = "EXTRA_IMG_URL"
        const val EXTRA_FILE = "EXTRA_FILE"

        fun start(activity: BaseActivity, image: Image){
            val intent = Intent(activity, FullScreenActivity::class.java)
            intent.putExtra(EXTRA_IMG_URL,
                "${image.imgUrl}&cacheBust=${UUID.randomUUID().hashCode()}")
            activity.startActivity(intent)
        }
    }
}
