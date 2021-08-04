package com.ivettevaldez.saturnus.screens.people.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ivettevaldez.saturnus.people.clienttype.ClientType
import com.ivettevaldez.saturnus.screens.common.controllers.BaseFragment
import com.ivettevaldez.saturnus.screens.common.controllers.ControllerFactory
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory
import javax.inject.Inject

class PeopleListFragment : BaseFragment() {

    @Inject
    lateinit var viewMvcFactory: ViewMvcFactory

    @Inject
    lateinit var controllerFactory: ControllerFactory

    private lateinit var controller: PeopleListController

    companion object {

        private const val ARG_CLIENT_TYPE = "ARG_CLIENT_TYPE"

        @JvmStatic
        fun newInstance(clientType: ClientType.Type) =
            PeopleListFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_CLIENT_TYPE, clientType)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)
        super.onCreate(savedInstanceState)

        controller = controllerFactory.newPeopleListController()
        controller.bindClientType(
            requireArguments().getSerializable(ARG_CLIENT_TYPE) as ClientType.Type
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val viewMvc = viewMvcFactory.newPeopleListViewMvc(parent)

        controller.bindView(viewMvc)

        return viewMvc.getRootView()
    }

    override fun onStart() {
        super.onStart()
        controller.onStart()
    }

    override fun onStop() {
        super.onStop()
        controller.onStop()
    }
}