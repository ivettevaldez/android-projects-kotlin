package com.ivettevaldez.saturnus.screens.people.list

import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.people.Person
import com.ivettevaldez.saturnus.screens.common.UtilsHelper
import com.ivettevaldez.saturnus.screens.common.viewsmvc.BaseObservableViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.IObservableViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory

interface IPeopleListViewMvc : IObservableViewMvc<IPeopleListViewMvc.Listener> {

    interface Listener {

        fun onPersonLongClick(rfc: String)
    }

    fun bindPeople(people: List<Person>)
    fun showProgressIndicator()
    fun hideProgressIndicator()
}

class PeopleListViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?,
    private val uiHandler: Handler,
    private val utilsHelper: UtilsHelper,
    private val viewMvcFactory: ViewMvcFactory
) : BaseObservableViewMvc<IPeopleListViewMvc.Listener>(
    inflater,
    parent,
    R.layout.layout_people_list
), IPeopleListViewMvc,
    IPeopleListItemViewMvc.Listener {

    private val layoutProgress: FrameLayout = findViewById(R.id.people_list_progress)
    private val recycler: RecyclerView = findViewById(R.id.people_list_recycler)
    private val textAddNew: TextView = findViewById(R.id.people_list_text_add_new)

    private lateinit var peopleListRecyclerAdapter: PeopleListRecyclerAdapter

    init {

        initRecycler()
    }

    override fun bindPeople(people: List<Person>) {
        uiHandler.post {
            if (people.isNotEmpty()) {
                peopleListRecyclerAdapter.updateData(people)
            } else {
                textAddNew.visibility = View.VISIBLE
                recycler.visibility = View.GONE
            }
        }
    }

    override fun showProgressIndicator() {
        uiHandler.post {
            layoutProgress.visibility = View.VISIBLE
        }
    }

    override fun hideProgressIndicator() {
        uiHandler.post {
            layoutProgress.visibility = View.GONE
        }
    }

    override fun onPersonLongClick(rfc: String) {
        for (listener in listeners) {
            listener.onPersonLongClick(rfc)
        }
    }

    private fun initRecycler() {
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.isSmoothScrollbarEnabled = true

        peopleListRecyclerAdapter = PeopleListRecyclerAdapter(
            viewMvcFactory,
            this
        )

        with(recycler) {
            layoutManager = linearLayoutManager
            adapter = peopleListRecyclerAdapter
            addItemDecoration(
                utilsHelper.getDividerItemDecoration()
            )
        }
    }
}