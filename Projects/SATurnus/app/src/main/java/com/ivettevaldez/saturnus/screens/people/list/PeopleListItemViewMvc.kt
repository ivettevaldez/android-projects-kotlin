package com.ivettevaldez.saturnus.screens.people.list

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.people.Person
import com.ivettevaldez.saturnus.screens.common.viewsmvc.BaseObservableViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.IObservableViewMvc

interface IPeopleListItemViewMvc : IObservableViewMvc<IPeopleListItemViewMvc.Listener> {

    interface Listener

    fun bindPerson(person: Person)
}

class PeopleListItemViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : BaseObservableViewMvc<IPeopleListItemViewMvc.Listener>(
    inflater,
    parent,
    R.layout.item_person
), IPeopleListItemViewMvc {

    private val textName: TextView = findViewById(R.id.item_person_name)
    private val textRfc: TextView = findViewById(R.id.item_person_rfc)
    private val textType: TextView = findViewById(R.id.item_person_type)

    override fun bindPerson(person: Person) {
        textName.text = person.name
        textRfc.text = person.rfc
        textType.text = person.personType
    }
}