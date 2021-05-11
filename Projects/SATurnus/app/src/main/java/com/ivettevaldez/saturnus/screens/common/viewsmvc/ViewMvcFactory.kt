package com.ivettevaldez.saturnus.screens.common.viewsmvc

import android.view.LayoutInflater
import android.view.ViewGroup
import com.ivettevaldez.saturnus.screens.common.main.MainViewMvc
import javax.inject.Inject
import javax.inject.Provider

class ViewMvcFactory @Inject constructor(
    private val inflater: Provider<LayoutInflater>
) {

    fun newMainViewMvc(parent: ViewGroup?): MainViewMvc {
        return MainViewMvc(inflater.get(), parent)
    }
}