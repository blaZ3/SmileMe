package me.tellvivk.smileme.app.screens.home

import android.content.Intent
import android.os.Bundle
import me.tellvivk.smileme.R
import me.tellvivk.smileme.app.base.BaseActivity
import me.tellvivk.smileme.app.base.BaseView
import me.tellvivk.smileme.app.base.StateModel
import me.tellvivk.smileme.app.base.ViewEvent
import me.tellvivk.smileme.app.model.ImageRepository
import me.tellvivk.smileme.dataSources.NetworkImageDataSource
import org.koin.android.ext.android.get

class HomeActivity : BaseActivity(), BaseView {

    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }

    override fun initView() {
        viewModel = get()


    }

    override fun getParentView(): BaseView? {
        return null
    }

    override fun updateView(stateModel: StateModel) {
        (stateModel as HomeStateModel).apply {

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


    companion object {
        fun start(activity: BaseActivity){
            activity.startActivity(Intent(activity, HomeActivity::class.java))
        }
    }
}
