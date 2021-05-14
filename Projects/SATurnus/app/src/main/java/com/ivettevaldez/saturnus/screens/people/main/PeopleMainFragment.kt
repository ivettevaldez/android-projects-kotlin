package com.ivettevaldez.saturnus.screens.people.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ivettevaldez.saturnus.screens.common.controllers.BaseFragment
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory
import javax.inject.Inject

class PeopleMainFragment : BaseFragment(),
    IPeopleMainViewMvc.Listener {

    @Inject
    lateinit var viewMvcFactory: ViewMvcFactory

    @Inject
    lateinit var peopleMainPagerAdapter: PeopleMainPagerAdapter

    private lateinit var viewMvc: IPeopleMainViewMvc

    companion object {

        @JvmStatic
        fun newInstance() = PeopleMainFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewMvc = viewMvcFactory.newPeopleViewMvc(parent)
        viewMvc.setViewPager(
            peopleMainPagerAdapter,
            PeopleMainPagerAdapter.TAB_CLIENT_TYPE_ISSUING
        )

        return viewMvc.getRootView()
    }

    override fun onStart() {
        super.onStart()
        viewMvc.registerListener(this)
    }

    override fun onStop() {
        super.onStop()
        viewMvc.unregisterListener(this)
    }

    override fun onTabSelected(position: Int) {
        // TODO:
    }

    override fun onAddNewClicked() {
        // TODO: Navigate to PeopleFormFragment
    }
}