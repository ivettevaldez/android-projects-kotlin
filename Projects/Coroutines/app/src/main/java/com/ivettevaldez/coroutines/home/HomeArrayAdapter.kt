package com.ivettevaldez.coroutines.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.ivettevaldez.coroutines.R

class HomeArrayAdapter(context: Context, private val listener: Listener) :
    ArrayAdapter<ScreenReachableFromHome>(context, 0) {

    interface Listener {

        fun onScreenClicked(screenReachableFromHome: ScreenReachableFromHome)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val newConvertView = convertView ?: LayoutInflater.from(context).inflate(
            R.layout.list_item_screen_reachable_from_home,
            parent,
            false
        )

        val screenReachableFromHome = getItem(position)!!

        // Display screen name
        val textName = newConvertView.findViewById<TextView>(R.id.text_screen_name)
        textName.text = screenReachableFromHome.description

        // Set click listener on individual item view
        newConvertView.setOnClickListener {
            listener.onScreenClicked(screenReachableFromHome)
        }

        return newConvertView
    }
}