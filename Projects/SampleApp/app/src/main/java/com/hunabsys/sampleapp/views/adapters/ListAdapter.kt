package com.hunabsys.sampleapp.views.adapters

import android.app.Activity
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.hunabsys.sampleapp.R
import com.hunabsys.sampleapp.pojos.ItemList

class ListAdapter(private val context: Activity,
                  private val values: ArrayList<ItemList>) : BaseAdapter() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return values.size
    }

    override fun getItem(position: Int): ItemList? {
        return values[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        val view: View?
        val viewHolder: PatientsRowHolder
        if (convertView == null) {
            view = this.inflater.inflate(R.layout.item_list, parent, false)
            viewHolder = PatientsRowHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as PatientsRowHolder
        }

        val item = getItem(position)
        setValues(viewHolder, item)

        return view
    }

    private fun setValues(viewHolder: PatientsRowHolder, item: ItemList?) {
        if (item != null) {
            viewHolder.textItem.text = item.item
            viewHolder.textSubItem.text = item.subitem

            viewHolder.layoutView.setOnClickListener {
                Snackbar.make(viewHolder.layoutView,
                        context.getString(R.string.message_i_do_nothing),
                        Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}

private class PatientsRowHolder(row: View?) {

    val layoutView: LinearLayout = row?.findViewById(R.id.item_list_layout) as LinearLayout
    val textItem: TextView = row?.findViewById(R.id.item_list_text_item) as TextView
    val textSubItem: TextView = row?.findViewById(R.id.item_list_text_subitem) as TextView
}