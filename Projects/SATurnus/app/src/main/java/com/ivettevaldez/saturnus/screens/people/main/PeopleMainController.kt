package com.ivettevaldez.saturnus.screens.people.main

import com.ivettevaldez.saturnus.screens.common.navigation.ScreensNavigator

class PeopleMainController(
    private val screensNavigator: ScreensNavigator
) : IPeopleMainViewMvc.Listener {

    private lateinit var viewMvc: IPeopleMainViewMvc

    fun bindView(viewMvc: IPeopleMainViewMvc) {
        this.viewMvc = viewMvc
    }

    fun initViewPager(adapter: PeopleMainPagerAdapter) {
        viewMvc.setViewPager(adapter, PeopleMainPagerAdapter.TAB_CLIENT_TYPE_ISSUING)
    }

    fun onStart() {
        viewMvc.registerListener(this)
    }

    fun onStop() {
        viewMvc.unregisterListener(this)
    }

    override fun onAddNewPersonClicked() {
        screensNavigator.toPersonForm(null)
    }

    override fun onNavigateUpClicked() {
        screensNavigator.navigateUp()
    }
}