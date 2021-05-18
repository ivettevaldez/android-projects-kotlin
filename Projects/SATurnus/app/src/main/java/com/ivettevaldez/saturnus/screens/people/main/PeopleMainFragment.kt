package com.ivettevaldez.saturnus.screens.people.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.ivettevaldez.saturnus.people.ClientType
import com.ivettevaldez.saturnus.screens.common.controllers.BaseFragment
import com.ivettevaldez.saturnus.screens.common.navigation.ScreensNavigator
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory
import javax.inject.Inject

class PeopleMainFragment : BaseFragment(),
    IPeopleMainViewMvc.Listener {

    @Inject
    lateinit var viewMvcFactory: ViewMvcFactory

    @Inject
    lateinit var screensNavigator: ScreensNavigator

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

        viewMvc = viewMvcFactory.newPeopleMainViewMvc(parent)

        // TODO: Fix this.
        viewMvc.setViewPager(
            PeopleMainPagerAdapter(requireActivity() as AppCompatActivity),
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

    override fun onAddNewClicked() {
        // TODO: Take this parameter dynamically.
        val clientType = ClientType.getString(ClientType.Type.ISSUING)
        screensNavigator.toPersonForm(null, clientType)
    }

    override fun onNavigateUpClicked() {
        screensNavigator.navigateUp()
    }
}