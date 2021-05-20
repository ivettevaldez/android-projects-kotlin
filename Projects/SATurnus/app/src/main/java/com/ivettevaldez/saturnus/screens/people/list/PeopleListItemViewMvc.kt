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
    private val textDetails: TextView = findViewById(R.id.item_person_text_details)
    private val imagePersonType: ImageView = findViewById(R.id.item_person_image_person_type)

    override fun bindPerson(person: Person) {
        textName.text = person.name
        textDetails.text = getPersonDetails(person.rfc, person.personType)
        imagePersonType.setImageResource(
            getPersonTypeIcon(person.personType)
        )
    }

    private fun getPersonDetails(rfc: String, personType: String) = String.format(
        context.getString(R.string.people_detail_template), rfc, personType
    )

    private fun getPersonTypeIcon(personType: String): Int {
        return if (personType == Constants.PHYSICAL_PERSON) {
            R.mipmap.ic_person_grey_36dp
        } else {
            R.mipmap.ic_people_grey_36dp
        }
    }
}