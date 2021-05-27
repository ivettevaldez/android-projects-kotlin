package com.ivettevaldez.saturnus.screens.people.item

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.legacy.widget.Space
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.common.Constants
import com.ivettevaldez.saturnus.people.Person
import com.ivettevaldez.saturnus.screens.common.viewsmvc.BaseViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.IViewMvc

interface IPersonItemViewMvc : IViewMvc {

    interface ActionClickListener {

        fun onActionClicked()
    }

    fun seTitle(title: String)
    fun setEmpty()
    fun bindPerson(person: Person)
    fun enableActionAndListen(listener: ActionClickListener)
}

class PersonItemViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : BaseViewMvc(
    inflater,
    parent,
    R.layout.item_invoice_person
), IPersonItemViewMvc {

    private val textTitle: TextView = findViewById(R.id.item_invoice_person_text_title)
    private val textName: TextView = findViewById(R.id.item_invoice_person_text_name)
    private val textRfc: TextView = findViewById(R.id.item_invoice_person_text_rfc)
    private val textAction: TextView = findViewById(R.id.item_invoice_person_text_action)
    private val imageType: ImageView = findViewById(R.id.item_invoice_person_image_person_type)
    private val layoutDetails: LinearLayout = findViewById(R.id.item_invoice_person_layout_details)
    private val space: Space = findViewById(R.id.item_invoice_person_space)

    private var actionClickListener: IPersonItemViewMvc.ActionClickListener? = null

    init {

        setListenerEvents()
    }

    override fun seTitle(title: String) {
        textTitle.text = title
    }

    override fun setEmpty() {
        textAction.text = context.getString(R.string.action_select_person)
        imageType.setImageResource(getPersonTypeIcon(""))
        layoutDetails.visibility = View.GONE
        space.visibility = View.GONE
    }

    override fun bindPerson(person: Person) {
        textName.text = person.name
        textRfc.text = String.format(
            "%s: %s",
            context.getString(R.string.people_rfc),
            person.rfc
        )
        imageType.setImageResource(getPersonTypeIcon(person.personType))

        layoutDetails.visibility = View.VISIBLE
        space.visibility = View.VISIBLE
        textAction.text = context.getString(R.string.action_change)
    }

    override fun enableActionAndListen(listener: IPersonItemViewMvc.ActionClickListener) {
        actionClickListener = listener
        textAction.visibility = View.VISIBLE
    }

    private fun setListenerEvents() {
        textAction.setOnClickListener {
            actionClickListener!!.onActionClicked()
        }
    }

    private fun getPersonTypeIcon(personType: String): Int {
        return when (personType) {
            Constants.PHYSICAL_PERSON -> R.mipmap.ic_person_grey_36dp
            Constants.MORAL_PERSON -> R.mipmap.ic_people_grey_36dp
            else -> R.mipmap.ic_person_plus_blue_36dp
        }
    }
}