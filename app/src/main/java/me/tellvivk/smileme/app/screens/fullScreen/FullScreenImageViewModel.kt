package me.tellvivk.smileme.app.screens.fullScreen

import me.tellvivk.smileme.app.base.BaseViewModel
import me.tellvivk.smileme.app.base.ProgressStateModel
import me.tellvivk.smileme.app.base.StateModel
import me.tellvivk.smileme.app.base.ViewEvent

class FullScreenImageViewModel: BaseViewModel() {

    init {
        model = FullScreenStateModel()
        initEvent = InitFullScreenEvent
    }




}

data class FullScreenStateModel(
    val progress: ProgressStateModel = ProgressStateModel()
): StateModel()

sealed class FullScreenViewEvent: ViewEvent
object InitFullScreenEvent: FullScreenViewEvent()