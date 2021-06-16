package com.ivettevaldez.saturnus.screens.people.item

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.legacy.widget.Space
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.common.Constants
import com.ivettevaldez.saturnus.people.Person
import com.ivettevaldez.saturnus.screens.common.viewsmvc.BaseViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.IViewMvc

interface IPersonItemViewMvc : IViewMvc {

    interface ItemClickListener {

        fun onItemClicked()
    }

    interface ItemLongClickListener {

        fun onItemLongClicked()
    }

    interface ActionClickListener {

        fun onActionClicked()
    }

    fun setBackgroundColor(@ColorRes color: Int)
    fun setEmpty()
    fun seTitle(title: String)
    fun hideTitle()
    fun bindPerson(person: Person)
    fun enableItemClickAndListen(listener: ItemClickListener)
    fun enableItemLongClickAndListen(listener: ItemLongClickListener)
    fun enableActionAndListen(actionName: String, listener: ActionClickListener)
}

class PersonItemViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : BaseViewMvc(
    inflater,
    parent,
    R.layout.item_person
), IPersonItemViewMvc {

    private val cardRoot: CardView = findViewById(R.id.item_invoice_person_layout_root)
    private val layoutDetails: LinearLayout = findViewById(R.id.item_invoice_person_layout_details)
    private val textTitle: TextView = findViewById(R.id.item_invoice_person_text_title)
    private val textName: TextView = findViewById(R.id.item_invoice_person_text_name)
    private val textRfc: TextView = findViewById(R.id.item_invoice_person_text_rfc)
    private val textAction: TextView = findViewById(R.id.item_invoice_person_text_action)
    private val imageType: ImageView = findViewById(R.id.item_invoice_person_image_person_type)
    private val space1: Space = findViewById(R.id.item_invoice_person_space_1)
    private val space2: Space = findViewById(R.id.item_invoice_person_space_2)

    private var actionName: String? = null

    private var itemClickListener: IPersonItemViewMvc.ItemClickListener? = null
    private var itemLongClickListener: IPersonItemViewMvc.ItemLongClickListener? = null
    private var actionClickListener: IPersonItemViewMvc.ActionClickListener? = null

    init {

        setListenerEvents()
    }

    override fun setBackgroundColor(@ColorRes color: Int) {
        cardRoot.setBackgroundColor(ContextCompat.getColor(context, color))
    }

    override fun setEmpty() {
        textAction.text = context.getString(R.string.action_select_person)
        imageType.setImageResource(getPersonTypeIcon(""))
        layoutDetails.visibility = View.GONE
        space2.visibility = View.GONE
    }

    override fun seTitle(title: String) {
        textTitle.text = title
    }

    override fun hideTitle() {
        textTitle.visibility = View.GONE
        space1.visibility = View.GONE
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
        space2.visibility = View.VISIBLE

        if (actionName != null) {
            textAction.text = actionName
        }
    }

    override fun enableItemClickAndListen(listener: IPersonItemViewMvc.ItemClickListener) {
        itemClickListener = listener
    }

    override fun enableItemLongClickAndListen(listener: IPersonItemViewMvc.ItemLongClickListener) {
        itemLongClickListener = listener
    }

    override fun enableActionAndListen(
        actionName: String,
        listener: IPersonItemViewMvc.ActionClickListener
    ) {
        this.actionClickListener = listener
        this.actionName = actionName

        textAction.visibility = View.VISIBLE
    }

    private fun setListenerEvents() {
        with(cardRoot) {
            setOnClickListener {
                itemClickListener?.onItemClicked()
            }
            setOnLongClickListener {
                itemLongClickListener?.onItemLongClicked()
                true
            }
        }

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