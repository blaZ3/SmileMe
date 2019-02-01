package me.tellvivk.smileme.helpers.logger

import android.util.Log
import me.tellvivk.smileme.helpers.logger.LoggerI

class AppLogger(private val isDebug: Boolean) : LoggerI {

    override fun d(tag: String, msg: String) {
        if (isDebug) {
            Log.d(tag, msg)
        }
    }

    override fun e(tag: String, msg: String) {
        Log.e(tag, msg)
    }
}