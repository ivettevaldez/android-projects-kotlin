package com.silviavaldez.sampleapp.views.adapters

import android.app.Activity
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.silviavaldez.sampleapp.R
import com.silviavaldez.sampleapp.helpers.TypefaceHelper
import com.silviavaldez.sampleapp.models.datamodels.Person
import io.realm.RealmResults

class ListAdapter(private val activity: Activity,
                  private val values: RealmResults<Person>) : BaseAdapter() {

    private val inflater: LayoutInflater = LayoutInflater.from(activity)

    override fun getCount(): Int {
        return values.size
    }

    override fun getItem(position: Int): Person? {
        return values[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        val view: View?
        val viewHolder: PersonRowHolder
        if (convertView == null) {
            view = this.inflater.inflate(R.layout.item_list, parent, false)
            viewHolder = PersonRowHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as PersonRowHolder
        }

        val item = getItem(position)
        setValues(viewHolder, item)
        setUpTypefaces(viewHolder)

        return view
    }

    private fun setValues(viewHolder: PersonRowHolder, item: Person?) {
        if (item != null) {
            val fullName = "${item.name} ${item.lastName}"
            viewHolder.textItem.text = fullName
            viewHolder.textSubItem.text = item.profession

            viewHolder.layoutView.setOnClickListener {
                Snackbar.make(viewHolder.layoutView,
                        activity.getString(R.string.message_i_do_nothing),
                        Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun setUpTypefaces(viewHolder: PersonRowHolder) {
        val typefaceHelper = TypefaceHelper(activity)
        typefaceHelper.overrideAllTypefaces(viewHolder.layoutView)
    }
}

private class PersonRowHolder(row: View?) {

    val layoutView: LinearLayout = row?.findViewById(R.id.item_list_layout) as LinearLayout
    val textItem: TextView = row?.findViewById(R.id.item_list_text_item) as TextView
    val textSubItem: TextView = row?.findViewById(R.id.item_list_text_subitem) as TextView
}