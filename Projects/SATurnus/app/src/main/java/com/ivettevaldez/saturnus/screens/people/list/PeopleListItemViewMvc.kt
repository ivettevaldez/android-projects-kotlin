package com.ivettevaldez.saturnus.screens.people.list

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.common.Constants
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

    private val textName: TextView = findViewById(R.id.item_person_text_name)
    private val textRfc: TextView = findViewById(R.id.item_person_text_rfc)
    private val textPersonType: TextView = findViewById(R.id.item_person_text_person_type)
    private val imagePersonType: ImageView = findViewById(R.id.item_person_image_person_type)

    override fun bindPerson(person: Person) {
        textName.text = person.name
        textRfc.text = getRfcWithFormat(person.rfc)
        textPersonType.text = person.personType
        imagePersonType.setImageResource(
            getPersonTypeIcon(person.personType)
        )
    }

    private fun getRfcWithFormat(rfc: String) = String.format(
        context.getString(R.string.people_rfc_template), rfc
    )

    private fun getPersonTypeIcon(personType: String): Int {
        return if (personType == Constants.PHYSICAL_PERSON) {
            R.mipmap.ic_person_white_36dp
        } else {
            R.mipmap.ic_people_white_36dp
        }
    }
}