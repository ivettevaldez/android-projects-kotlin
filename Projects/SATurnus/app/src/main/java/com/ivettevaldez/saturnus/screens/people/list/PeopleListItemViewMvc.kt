package com.ivettevaldez.saturnus.screens.people.list

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.people.Person
import com.ivettevaldez.saturnus.screens.common.viewsmvc.BaseObservableViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.IObservableViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory
import com.ivettevaldez.saturnus.screens.people.item.IPersonItemViewMvc

interface IPeopleListItemViewMvc : IObservableViewMvc<IPeopleListItemViewMvc.Listener> {

    interface Listener {

        fun onPersonClick(rfc: String)
        fun onPersonLongClick(rfc: String)
    }

    fun bindPerson(person: Person)
}

class PeopleListItemViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?,
    viewMvcFactory: ViewMvcFactory
) : BaseObservableViewMvc<IPeopleListItemViewMvc.Listener>(
    inflater,
    parent,
    R.layout.element_container
), IPeopleListItemViewMvc {

    private val personItem: FrameLayout = findViewById(R.id.container)
    private val personItemViewMvc: IPersonItemViewMvc = viewMvcFactory.newPersonItemViewMvc(
        personItem
    )

    private lateinit var person: Person

    init {

        initView()
        setListenerEvents()
    }

    private fun initView() {
        personItemViewMvc.hideTitle()
        personItem.addView(personItemViewMvc.getRootView())
    }

    override fun bindPerson(person: Person) {
        this.person = person
        personItemViewMvc.bindPerson(person)
    }

    private fun setListenerEvents() {
        personItemViewMvc.enableItemClickAndListen(object :
            IPersonItemViewMvc.ItemClickListener {
            override fun onItemClicked() {
                for (listener in listeners) {
                    listener.onPersonClick(person.rfc)
                }
            }
        })

        personItemViewMvc.enableItemLongClickAndListen(object :
            IPersonItemViewMvc.ItemLongClickListener {
            override fun onItemLongClicked() {
                for (listener in listeners) {
                    listener.onPersonLongClick(person.rfc)
                }
            }
        })
    }
}