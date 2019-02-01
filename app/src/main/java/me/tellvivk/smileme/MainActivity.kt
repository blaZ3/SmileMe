package me.tellvivk.smileme

import android.os.Bundle
import me.tellvivk.smileme.app.base.BaseActivity
import me.tellvivk.smileme.app.screens.home.HomeActivity

class MainActivity : BaseActivity() {

    //http://www.json-generator.com/api/json/get/cftPFNNHsi
    // "http://www.json-generator.com/api/json/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HomeActivity.start(this)
    }
}
