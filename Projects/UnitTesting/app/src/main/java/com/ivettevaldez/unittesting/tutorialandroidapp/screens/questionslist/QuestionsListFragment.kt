package com.ivettevaldez.unittesting.tutorialandroidapp.screens.questionslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ivettevaldez.unittesting.R
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.controllers.BaseFragment

class QuestionsListFragment : BaseFragment() {

    companion object {

        @JvmStatic
        fun newInstance() = QuestionsListFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.layout_questions_list, container, false)
    }
}