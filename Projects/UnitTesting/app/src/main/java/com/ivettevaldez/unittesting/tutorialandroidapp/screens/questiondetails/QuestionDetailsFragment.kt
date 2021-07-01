package com.ivettevaldez.unittesting.tutorialandroidapp.screens.questiondetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.controllers.BaseFragment

class QuestionDetailsFragment : BaseFragment() {

    private lateinit var controller: QuestionDetailsController

    companion object {

        private const val ARG_QUESTION_ID = "ARG_QUESTION_ID"

        @JvmStatic
        fun newInstance(questionId: String) = QuestionDetailsFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_QUESTION_ID, questionId)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        controller = getCompositionRoot().getQuestionDetailsController()
        controller.bindQuestionId(getQuestionId())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val viewMvc = getCompositionRoot()
            .getViewMvcFactory()
            .getQuestionDetailsViewMvc(parent)

        controller.bindView(viewMvc)

        return viewMvc.getRootView()
    }

    private fun getQuestionId(): String = requireArguments().getString(ARG_QUESTION_ID)!!
}