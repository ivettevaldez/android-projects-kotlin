package com.ivettevaldez.cleanarchitecture.screens.questiondetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.ivettevaldez.cleanarchitecture.R
import com.ivettevaldez.cleanarchitecture.questions.FetchQuestionDetailsUseCase
import com.ivettevaldez.cleanarchitecture.questions.Question
import com.ivettevaldez.cleanarchitecture.screens.common.controllers.BaseFragment
import com.ivettevaldez.cleanarchitecture.screens.common.controllers.IBackPressDispatcher
import com.ivettevaldez.cleanarchitecture.screens.common.controllers.IBackPressedListener
import com.ivettevaldez.cleanarchitecture.screens.common.navigation.ScreenNavigator

private const val ARG_QUESTION_ID = "ARG_QUESTION_ID"

class QuestionDetailsFragment : BaseFragment(),
    IBackPressedListener,
    IQuestionDetailsViewMvc.Listener,
    FetchQuestionDetailsUseCase.Listener {

    private var questionId: String? = null

    private lateinit var fetchQuestionDetailsUseCase: FetchQuestionDetailsUseCase
    private lateinit var screenNavigator: ScreenNavigator
    private lateinit var backPressDispatcher: IBackPressDispatcher
    private lateinit var viewMvc: IQuestionDetailsViewMvc

    companion object {

        @JvmStatic
        fun newInstance(questionId: String) =
            QuestionDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_QUESTION_ID, questionId)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            questionId = it.getString(ARG_QUESTION_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        fetchQuestionDetailsUseCase = getCompositionRoot().getFetchQuestionDetailsUseCase()
        screenNavigator = getCompositionRoot().getScreenNavigator()
        backPressDispatcher = getCompositionRoot().getBackPressDispatcher()

        viewMvc = getCompositionRoot()
            .getViewMvcFactory()
            .getQuestionDetailsViewMvc(container)

        return viewMvc.getRootView()
    }

    override fun onStart() {
        super.onStart()

        fetchQuestionDetailsUseCase.registerListener(this)
        backPressDispatcher.registerListener(this)

        viewMvc.registerListener(this)
        viewMvc.showProgressIndicator(true)

        fetchQuestionDetails(questionId!!)
    }

    override fun onStop() {
        super.onStop()

        fetchQuestionDetailsUseCase.unregisterListener(this)
        backPressDispatcher.unregisterListener(this)
        viewMvc.unregisterListener(this)
    }

    override fun onBackPressed(): Boolean {
        if (viewMvc.isDrawerOpen()) {
            viewMvc.closeDrawer()
            return true
        }
        return false
    }

    override fun onNavigateUpClicked() {
        screenNavigator.toQuestionsList()
    }

    override fun onQuestionsListClicked() {
        screenNavigator.toQuestionsList()
    }

    override fun onQuestionDetailsFetched(question: Question) {
        viewMvc.showProgressIndicator(false)
        viewMvc.bindQuestion(question)
    }

    override fun onQuestionDetailsFetchFailed() {
        viewMvc.showProgressIndicator(false)
        showMessage(
            getString(R.string.error_network_callback_failed)
        )
    }

    private fun fetchQuestionDetails(questionId: String) {
        fetchQuestionDetailsUseCase.executeAndNotify(questionId)
    }

    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}