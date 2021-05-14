package com.ivettevaldez.saturnus.screens.people.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.screens.common.navigation.INavDrawerHelper
import com.ivettevaldez.saturnus.screens.common.toolbar.IToolbarViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.BaseObservableViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.IObservableViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory

interface IPeopleMainViewMvc : IObservableViewMvc<IPeopleMainViewMvc.Listener> {

    interface Listener {

        fun onAddNewClicked()
    }

    fun setViewPager(adapter: PeopleMainPagerAdapter, selectedTabPosition: Int)
}

class PeopleMainViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?,
    viewMvcFactory: ViewMvcFactory,
    private val navDrawerHelper: INavDrawerHelper
) : BaseObservableViewMvc<IPeopleMainViewMvc.Listener>(
    inflater,
    parent,
    R.layout.layout_people_main
), IPeopleMainViewMvc {

    private val toolbar: Toolbar = findViewById(R.id.people_toolbar)
    private val viewPager: ViewPager2 = findViewById(R.id.people_main_view_pager)
    private val tabLayout: TabLayout = findViewById(R.id.people_main_tab_layout)
    private val fabAddNew: FloatingActionButton = findViewById(R.id.people_main_fab_add_new)

    private val toolbarViewMvc: IToolbarViewMvc = viewMvcFactory.newToolbarViewMvc(toolbar)

    init {

        initToolbar()
        setListenerEvents()
    }

    override fun setViewPager(adapter: PeopleMainPagerAdapter, selectedTabPosition: Int) {
        viewPager.adapter = adapter

        initTabs()
        selectTab(selectedTabPosition)
    }

    private fun initToolbar() {
        toolbarViewMvc.setTitle(
            context.getString(R.string.menu_people)
        )

        toolbarViewMvc.enableMenuAndListen(object : IToolbarViewMvc.MenuClickListener {
            override fun onMenuClicked() {
                for (listener in listeners) {
                    navDrawerHelper.openDrawer()
                }
            }
        })

        toolbar.addView(toolbarViewMvc.getRootView())
    }

    private fun setListenerEvents() {
        fabAddNew.setOnClickListener {
            for (listener in listeners) {
                listener.onAddNewClicked()
            }
        }
    }

    private fun getTabsTitles(): Array<String> =
        context.resources.getStringArray(R.array.people_tabs_titles)

    private fun initTabs() {
        val tabsTitles = getTabsTitles()

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabsTitles[position]
        }.attach()
    }

    private fun selectTab(tabPosition: Int) {
        viewPager.currentItem = tabPosition
    }
}