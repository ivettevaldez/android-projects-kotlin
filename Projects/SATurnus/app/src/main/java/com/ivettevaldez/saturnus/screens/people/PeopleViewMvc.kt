package com.ivettevaldez.saturnus.screens.people

import android.view.LayoutInflater
import android.view.ViewGroup
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.screens.common.viewsmvc.BaseObservableViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.IObservableViewMvc

interface IPeopleViewMvc : IObservableViewMvc<IPeopleViewMvc.Listener> {

    interface Listener
}

class PeopleViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : BaseObservableViewMvc<IPeopleViewMvc.Listener>(
    inflater,
    parent,
    R.layout.layout_people
), IPeopleViewMvc