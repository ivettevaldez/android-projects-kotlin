package com.ivettevaldez.saturnus.screens.common.dialogs.personselector

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ivettevaldez.saturnus.people.Person
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory

class PersonSelectorListRecyclerAdapter(
    private val viewMvcFactory: ViewMvcFactory,
    private val listener: IPersonSelectorListItemViewMvc.Listener
) : RecyclerView.Adapter<PersonSelectorListRecyclerAdapter.ViewHolder>(),
    IPersonSelectorListItemViewMvc.Listener {

    private val people: MutableList<Person> = mutableListOf()

    inner class ViewHolder(val viewMvc: IPersonSelectorListItemViewMvc) :
        RecyclerView.ViewHolder(viewMvc.getRootView())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewMvc = viewMvcFactory.newPersonSelectorListItemViewMvc(parent)
        viewMvc.registerListener(this)
        return ViewHolder(viewMvc)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.viewMvc.bindPerson(
            people[position]
        )
    }

    override fun getItemCount(): Int = people.size

    override fun onPersonSelected(rfc: String) {
        listener.onPersonSelected(rfc)
    }

    fun updateData(people: List<Person>) {
        this.people.clear()
        this.people.addAll(people)
        notifyDataSetChanged()
    }
}