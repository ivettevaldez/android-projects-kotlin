package com.ivettevaldez.saturnus.screens.common.main

import android.view.LayoutInflater
import android.view.ViewGroup
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.screens.common.viewsmvc.BaseObservableViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.IObservableViewMvc

class MainViewMvc(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : BaseObservableViewMvc<MainViewMvc.Listener>(
    inflater,
    parent,
    R.layout.activity_main
), IObservableViewMvc<MainViewMvc.Listener> {

    interface Listener
}