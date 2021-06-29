package com.ivettevaldez.unittesting.tutorialandroidapp.screens.common

import android.view.LayoutInflater
import android.view.ViewGroup
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.navdrawer.NavDrawerHelper
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.navdrawer.NavDrawerViewMvc
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.navdrawer.NavDrawerViewMvcImpl

class ViewMvcFactory(
    private val inflater: LayoutInflater,
    private val navDrawerHelper: NavDrawerHelper
) {

    fun getNavDrawerViewMvc(parent: ViewGroup?): NavDrawerViewMvc {
        return NavDrawerViewMvcImpl(inflater, parent)
    }
}