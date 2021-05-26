package com.ivettevaldez.saturnus.screens.common.dialogs.personselector

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.common.Constants
import com.ivettevaldez.saturnus.people.Person
import com.ivettevaldez.saturnus.screens.common.viewsmvc.BaseObservableViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.IObservableViewMvc

interface IPersonSelectorListItemViewMvc :
    IObservableViewMvc<IPersonSelectorListItemViewMvc.Listener> {

    interface Listener {

        fun onPersonSelected(rfc: String)
    }

    fun bindPerson(person: Person)
}

class PersonSelectorListItemViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : BaseObservableViewMvc<IPersonSelectorListItemViewMvc.Listener>(
    inflater,
    parent,
    R.layout.item_person_selector
), IPersonSelectorListItemViewMvc {

    private val textName: TextView = findViewById(R.id.item_person_selector_text_name)
    private val textRfc: TextView = findViewById(R.id.item_person_selector_text_rfc)
    private val buttonSelect: Button = findViewById(R.id.item_person_selector_button_select)
    private val imagePersonType: ImageView =
        findViewById(R.id.item_person_selector_image_person_type)

    private lateinit var person: Person

    init {

        setListenerEvents()
    }

    override fun bindPerson(person: Person) {
        this.person = person

        textName.text = person.name
        textRfc.text = String.format("%s: %s", context.getString(R.string.people_rfc), person.rfc)
        imagePersonType.setImageResource(
            getPersonTypeIcon(person.personType)
        )
    }

    private fun setListenerEvents() {
        buttonSelect.setOnClickListener {
            for (listener in listeners) {
                listener.onPersonSelected(person.rfc)
            }
        }
    }

    private fun getPersonTypeIcon(personType: String): Int {
        return if (personType == Constants.PHYSICAL_PERSON) {
            R.mipmap.ic_person_grey_36dp
        } else {
            R.mipmap.ic_people_grey_36dp
        }
    }
}