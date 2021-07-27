package com.ivettevaldez.saturnus.screens.people.form

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ivettevaldez.saturnus.screens.common.controllers.BaseFragment
import com.ivettevaldez.saturnus.screens.common.controllers.ControllerFactory
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory
import javax.inject.Inject

class PersonFormFragment : BaseFragment() {

    @Inject
    lateinit var controllerFactory: ControllerFactory

    @Inject
    lateinit var viewMvcFactory: ViewMvcFactory

    private lateinit var controller: PersonFormController

    companion object {

        private const val ARG_RFC = "ARG_RFC"

        @JvmStatic
        fun newInstance(rfc: String?) = PersonFormFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_RFC, rfc)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)
        super.onCreate(savedInstanceState)

        controller = controllerFactory.newPersonFormController()
        controller.bindRfc(
            requireArguments().getString(ARG_RFC)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val viewMvc = viewMvcFactory.newPersonFormViewMvc(parent)

        controller.bindView(viewMvc)
        controller.setToolbarTitle()

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