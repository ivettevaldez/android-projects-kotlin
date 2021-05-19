package com.ivettevaldez.saturnus.screens.people.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ivettevaldez.saturnus.people.ClientType
import com.ivettevaldez.saturnus.people.Person
import com.ivettevaldez.saturnus.people.PersonDao
import com.ivettevaldez.saturnus.screens.common.controllers.BaseFragment
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory
import javax.inject.Inject

private const val ARG_CLIENT_TYPE = "ARG_CLIENT_TYPE"

class PeopleListFragment : BaseFragment(),
    IPeopleListViewMvc.Listener {

    @Inject
    lateinit var viewMvcFactory: ViewMvcFactory

    @Inject
    lateinit var peopleDao: PersonDao

    private lateinit var viewMvc: IPeopleListViewMvc
    private lateinit var clientType: ClientType.Type

    companion object {

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

        if (arguments == null) {
            throw RuntimeException("@@@@@ Arguments must not be null: required $ARG_CLIENT_TYPE")
        } else {
            arguments!!.let {
                clientType = it.getSerializable(ARG_CLIENT_TYPE) as ClientType.Type
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewMvc = viewMvcFactory.newPeopleListViewMvc(parent)

        bindPeople()

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

    override fun onResume() {
        super.onResume()
        bindPeople()
    }

    private fun getPeople(): List<Person> = when (clientType) {
        ClientType.Type.ISSUING -> peopleDao.findAllIssuing()
        ClientType.Type.RECEIVER -> peopleDao.findAllReceivers()
    }

    private fun bindPeople() {
        Thread {
            viewMvc.showProgressIndicator()
            viewMvc.bindPeople(getPeople())
            viewMvc.hideProgressIndicator()
        }.start()
    }
}