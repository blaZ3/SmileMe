package me.tellvivk.smileme.app.base

import androidx.appcompat.app.AppCompatActivity
import me.tellvivk.smileme.R

open class BaseActivity: AppCompatActivity() {

    protected fun initToolbar(title: String, toolbar: androidx.appcompat.widget.Toolbar) {
        setSupportActionBar(toolbar)
        supportActionBar?.title = title
        toolbar.navigationIcon = resources.getDrawable(R.drawable.ic_arrow_back_white)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }



}