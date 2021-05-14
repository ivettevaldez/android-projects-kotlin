package com.ivettevaldez.saturnus.screens.people.list

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ivettevaldez.saturnus.people.Person
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory

class PeopleListRecyclerAdapter(
    private val viewMvcFactory: ViewMvcFactory
) : RecyclerView.Adapter<PeopleListRecyclerAdapter.ViewHolder>(),
    IPeopleListItemViewMvc.Listener {

    private val people: MutableList<Person> = mutableListOf()

    inner class ViewHolder(val viewMvc: IPeopleListItemViewMvc) :
        RecyclerView.ViewHolder(viewMvc.getRootView())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewMvc = viewMvcFactory.newPeopleListItemViewMvc(parent)
        viewMvc.registerListener(this)
        return ViewHolder(viewMvc)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.viewMvc.bindPerson(
            people[position]
        )
    }

    override fun getItemCount(): Int = people.size

    fun updateData(people: List<Person>) {
        this.people.clear()
        this.people.addAll(people)
        notifyDataSetChanged()
    }
}