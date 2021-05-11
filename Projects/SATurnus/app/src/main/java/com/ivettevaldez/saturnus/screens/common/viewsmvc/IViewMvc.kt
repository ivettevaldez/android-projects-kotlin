package com.ivettevaldez.saturnus.screens.common.viewsmvc

import android.view.View
import androidx.annotation.IdRes

interface IViewMvc {

    fun <T : View?> findViewById(@IdRes id: Int): T
}