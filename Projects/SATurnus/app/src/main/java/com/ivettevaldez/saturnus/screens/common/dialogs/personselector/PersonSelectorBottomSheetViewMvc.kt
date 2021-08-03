package com.ivettevaldez.saturnus.screens.common.dialogs.personselector

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.people.Person
import com.ivettevaldez.saturnus.screens.common.viewsmvc.BaseObservableViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.IObservableViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory

interface IPersonSelectorBottomSheetViewMvc :
    IObservableViewMvc<IPersonSelectorBottomSheetViewMvc.Listener> {

    interface Listener {

        fun onPersonSelected(rfc: String)
    }

    fun bindTitle(title: String)
    fun bindPeople(people: List<Person>)
}

class PersonSelectorBottomSheetViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?,
    private val viewMvcFactory: ViewMvcFactory
) : BaseObservableViewMvc<IPersonSelectorBottomSheetViewMvc.Listener>(
    inflater,
    parent,
    R.layout.dialog_person_selector_bottom_sheet
), IPersonSelectorBottomSheetViewMvc,
    IPersonSelectorListItemViewMvc.Listener {

    private val textTitle: TextView = findViewById(R.id.person_selector_bottom_sheet_text_title)
    private val recycler: RecyclerView =
        findViewById(R.id.person_selector_bottom_sheet_recycler)

    private lateinit var peopleListRecyclerAdapter: PersonSelectorListRecyclerAdapter

    init {

        initRecycler()
    }

    override fun bindTitle(title: String) {
        textTitle.text = title
    }

    override fun bindPeople(people: List<Person>) {
        peopleListRecyclerAdapter.updateData(people)
    }

    override fun onPersonSelected(rfc: String) {
        for (listener in listeners) {
            listener.onPersonSelected(rfc)
        }
    }

    private fun initRecycler() {
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.isSmoothScrollbarEnabled = true

        peopleListRecyclerAdapter = PersonSelectorListRecyclerAdapter(
            viewMvcFactory,
            this
        )

        with(recycler) {
            layoutManager = linearLayoutManager
            adapter = peopleListRecyclerAdapter
        }
    }
}