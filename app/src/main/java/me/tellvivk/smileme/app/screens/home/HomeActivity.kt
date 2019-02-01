package me.tellvivk.smileme.app.screens.home

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.activity_home.*
import me.tellvivk.smileme.R
import me.tellvivk.smileme.app.base.BaseActivity
import me.tellvivk.smileme.app.base.BaseView
import me.tellvivk.smileme.app.base.StateModel
import me.tellvivk.smileme.app.base.ViewEvent
import me.tellvivk.smileme.app.model.Image
import me.tellvivk.smileme.app.screens.home.adapter.HomeImagesAdapter
import me.tellvivk.smileme.app.screens.home.adapter.ImagesListDiffCallback
import org.koin.android.ext.android.get

class HomeActivity : BaseActivity(), BaseView {

    private lateinit var viewModel: HomeViewModel
    private lateinit var homeAdapter: HomeImagesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }

    override fun initView() {
        viewModel = get()

        homeAdapter = HomeImagesAdapter(listOf(), this, homeImagesAdapter)

        recyclerHome.layoutManager = StaggeredGridLayoutManager(2,
            StaggeredGridLayoutManager.VERTICAL)
        recyclerHome.adapter = homeAdapter
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
        }
    }

    override fun handleEvent(event: ViewEvent) {
        (event as InitHomeEvent).apply {
            when(this){
                InitHomeEvent -> {
                    viewModel.getImages()
                }
            }
        }
    }

    private val homeImagesAdapter = object : HomeImagesAdapter.HomeImagesAdapterInterface{
        override fun onImageClicked(image: Image) {
            Toast.makeText(this@HomeActivity, "image clicked",
                Toast.LENGTH_SHORT).show()
        }
    }


    companion object {
        fun start(activity: BaseActivity){
            activity.startActivity(Intent(activity, HomeActivity::class.java))
        }
    }
}
