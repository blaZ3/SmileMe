package me.tellvivk.smileme.app.screens.fullScreen

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import me.tellvivk.smileme.R
import me.tellvivk.smileme.app.base.BaseActivity

class FullScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen)
    }


    companion object {
        fun start(activity: BaseActivity){
            activity.startActivity(Intent(activity, FullScreenActivity::class.java))
        }
    }
}
