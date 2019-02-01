package me.tellvivk.smileme.app.screens.home

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.autoDisposable
import kotlinx.android.synthetic.main.activity_home.*
import me.tellvivk.smileme.R
import me.tellvivk.smileme.app.base.BaseActivity
import me.tellvivk.smileme.app.base.BaseView
import me.tellvivk.smileme.app.base.StateModel
import me.tellvivk.smileme.app.base.ViewEvent
import me.tellvivk.smileme.app.model.Image
import me.tellvivk.smileme.app.screens.fullScreen.FullScreenActivity
import me.tellvivk.smileme.app.screens.home.adapter.HomeImagesAdapter
import me.tellvivk.smileme.app.screens.home.adapter.ImagesListDiffCallback
import me.tellvivk.smileme.databinding.ActivityHomeBinding
import org.koin.android.ext.android.get

class HomeActivity : BaseActivity(), BaseView {

    private lateinit var viewModel: HomeViewModel
    private lateinit var homeAdapter: HomeImagesAdapter

    private lateinit var dataBinding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_home)
    }

    override fun onStart() {
        super.onStart()
        initView()
    }

    override fun initView() {
        viewModel = get()

        homeAdapter = HomeImagesAdapter(listOf(), this, homeImagesAdapter)

        recyclerHome.layoutManager = StaggeredGridLayoutManager(2,
            StaggeredGridLayoutManager.VERTICAL)
        recyclerHome.adapter = homeAdapter

        viewModel.getViewModelObservable()
            .autoDisposable(AndroidLifecycleScopeProvider.from(this))
            .subscribe { updateView(it) }

        viewModel.getViewEventObservable()
            .autoDisposable(AndroidLifecycleScopeProvider.from(this))
            .subscribe{ handleEvent(it) }

        fabAddNewPic.setOnClickListener {
            //camera intent
        }
    }

    override fun getParentView(): BaseView? {
        return null
    }

    override fun updateView(stateModel: StateModel) {
        (stateModel as HomeStateModel).apply {

            val updatedItems = ArrayList<Image>()
            //add plus icon
            updatedItems.addAll(stateModel.images)

            val diffResult = DiffUtil.calculateDiff(
                ImagesListDiffCallback(
                    homeAdapter.items,
                    updatedItems
                )
            )
            diffResult.dispatchUpdatesTo(homeAdapter)
            homeAdapter.items = stateModel.images


            dataBinding.stateModel = this
        }
    }

    override fun handleEvent(event: ViewEvent) {
        (event as HomeViewEvent).apply {
            when(this){
                InitHomeEvent -> {
                    viewModel.getImages()
                }
                is LoadingErrorEvent -> {
                    showToast(this.msg)
                }
            }
        }
    }

    private val homeImagesAdapter = object : HomeImagesAdapter.HomeImagesAdapterInterface{
        override fun onImageClicked(image: Image) {
            Toast.makeText(this@HomeActivity, "image clicked",
                Toast.LENGTH_SHORT).show()
            FullScreenActivity.start(this@HomeActivity)
        }
    }


    companion object {
        fun start(activity: BaseActivity){
            activity.startActivity(Intent(activity, HomeActivity::class.java))
        }
    }
}
