package me.tellvivk.smileme.helpers.stringFetcher

import android.content.Context
import me.tellvivk.smileme.helpers.stringFetcher.StringFetcherI

class AppStringFetcher(private val context: Context) : StringFetcherI {

    override fun getString(resId: Int): String {
        return context.resources.getString(resId)
    }
}