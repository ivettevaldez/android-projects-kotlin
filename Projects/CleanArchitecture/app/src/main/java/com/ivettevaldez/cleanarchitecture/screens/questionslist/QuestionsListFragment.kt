package com.ivettevaldez.cleanarchitecture.screens.questionslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.ivettevaldez.cleanarchitecture.R
import com.ivettevaldez.cleanarchitecture.questions.FetchQuestionsUseCase
import com.ivettevaldez.cleanarchitecture.questions.Question
import com.ivettevaldez.cleanarchitecture.screens.common.controllers.BaseFragment

class QuestionsListFragment : BaseFragment(),
    IQuestionsListViewMvc.Listener,
    FetchQuestionsUseCase.Listener {

    private lateinit var fetchQuestionsUseCase: FetchQuestionsUseCase
    private lateinit var viewMvc: IQuestionsListViewMvc

    companion object {

        fun newInstance(): QuestionsListFragment = QuestionsListFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        fetchQuestionsUseCase = getCompositionRoot().getFetchQuestionsUseCase()

        viewMvc = getCompositionRoot()
            .getViewMvcFactory()
            .getQuestionsListViewMvc(container)

        return viewMvc.getRootView()
    }

    override fun onStart() {
        super.onStart()

        fetchQuestionsUseCase.registerListener(this)

        viewMvc.registerListener(this)
        viewMvc.showProgressIndicator(true)

        fetchQuestions()
    }

    override fun onStop() {
        super.onStop()

        fetchQuestionsUseCase.unregisterListener(this)
        viewMvc.unregisterListener(this)
    }

    override fun onQuestionClicked(question: Question?) {
        if (question != null) {
            getCompositionRoot()
                .getScreenNavigator()
                .toQuestionDetails(question.id)
        } else {
            showMessage(getString(R.string.error_null_question))
        }
    }

    override fun onQuestionsFetched(questions: List<Question>) {
        viewMvc.showProgressIndicator(false)
        viewMvc.bindQuestions(questions)
    }

    override fun onQuestionsFetchFailed() {
        viewMvc.showProgressIndicator(false)
        showMessage(
            getString(R.string.error_network_callback_failed)
        )
    }

    private fun fetchQuestions() {
        fetchQuestionsUseCase.executeAndNotify()
    }

    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}