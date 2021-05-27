package com.ivettevaldez.saturnus.screens.common.dialogs.personselector

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.people.Person
import com.ivettevaldez.saturnus.screens.common.viewsmvc.BaseObservableViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.IObservableViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory
import com.ivettevaldez.saturnus.screens.people.item.IPersonItemViewMvc

interface IPersonSelectorListItemViewMvc :
    IObservableViewMvc<IPersonSelectorListItemViewMvc.Listener> {

    interface Listener {

        fun onPersonSelected(rfc: String)
    }

    fun bindPerson(person: Person)
}

class PersonSelectorListItemViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?,
    viewMvcFactory: ViewMvcFactory
) : BaseObservableViewMvc<IPersonSelectorListItemViewMvc.Listener>(
    inflater,
    parent,
    R.layout.element_container
), IPersonSelectorListItemViewMvc {

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
        personItemViewMvc.enableActionAndListen(
            context.getString(R.string.action_assign),
            object : IPersonItemViewMvc.ActionClickListener {
                override fun onActionClicked() {
                    for (listener in listeners) {
                        listener.onPersonSelected(person.rfc)
                    }
                }
            })
    }
}