package com.ivettevaldez.saturnus.screens.people.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ivettevaldez.saturnus.R

private const val CLIENT_TYPE = "CLIENT_TYPE"

class PeopleListFragment : Fragment() {

    private var clientType: String? = null

    companion object {

        @JvmStatic
        fun newInstance(clientType: String) =
            PeopleListFragment().apply {
                arguments = Bundle().apply {
                    putString(CLIENT_TYPE, clientType)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            clientType = it.getString(CLIENT_TYPE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.layout_people_list, container, false)
    }
}