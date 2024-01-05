package com.ivettevaldez.unittesting.tutorialandroidapp.screens.questionslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.controllers.BaseFragment

class QuestionsListFragment : BaseFragment() {

    private lateinit var controller: QuestionsListController

    companion object {

        @JvmStatic
        fun newInstance() = QuestionsListFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val viewMvc = getCompositionRoot().getViewMvcFactory().getQuestionsListViewMvc(parent)

        controller = getCompositionRoot().getQuestionsListController()
        controller.bindView(viewMvc)

        return viewMvc.getRootView()
    }

    override fun onStart() {
        super.onStart()
        controller.onStart()
    }

    override fun onStop() {
        super.onStop()
        controller.onStop()
    }
}