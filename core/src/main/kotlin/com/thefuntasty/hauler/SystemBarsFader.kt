package com.thefuntasty.hauler

import android.app.Activity
import android.graphics.Color

internal class SystemBarsFader(private val activity: Activity, private val keepTransparent: Boolean) {

    private val statusBarAlpha: Int by lazy { Color.alpha(getStatusBarColor()) }

    init {
        if(keepTransparent){
            activity.window.statusBarColor = ColorUtils.modifyAlpha(getStatusBarColor(), 1)
        }
    }

    fun onDrag(elasticOffsetPixels: Float, rawOffset: Float) {
        when {
            elasticOffsetPixels != 0f -> // dragging downward or upward, fade the status bar in proportion
                activity.window.statusBarColor = ColorUtils.modifyAlpha(getStatusBarColor(), getNewAlpha(if(keepTransparent) 1f else rawOffset))
            elasticOffsetPixels == 0f ->
                activity.window.statusBarColor = ColorUtils.modifyAlpha(getStatusBarColor(), if(keepTransparent) 1 else statusBarAlpha)
        }
    }

    fun onDismiss() {
        // set transparent window bg and transparent navigation bar
        activity.window.decorView.setBackgroundColor(0)
        activity.window.navigationBarColor = ColorUtils.modifyAlpha(activity.window.navigationBarColor, if(keepTransparent) 1 else 0)
    }

    private fun getStatusBarColor() = activity.window.statusBarColor

    private fun getNewAlpha(rawOffset: Float) = ((1f - rawOffset) * statusBarAlpha).toInt()
}
