package com.ivettevaldez.cleanarchitecture.screens.questiondetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ivettevaldez.cleanarchitecture.questions.FetchQuestionDetailsUseCase
import com.ivettevaldez.cleanarchitecture.questions.Question
import com.ivettevaldez.cleanarchitecture.screens.common.controllers.BaseFragment
import com.ivettevaldez.cleanarchitecture.screens.common.dialogs.DialogsManager
import com.ivettevaldez.cleanarchitecture.screens.common.navigation.ScreensNavigator

private const val ARG_QUESTION_ID = "ARG_QUESTION_ID"

class QuestionDetailsFragment private constructor() : BaseFragment(),
    IQuestionDetailsViewMvc.Listener,
    FetchQuestionDetailsUseCase.Listener {

    private var questionId: String? = null

    private lateinit var fetchQuestionDetailsUseCase: FetchQuestionDetailsUseCase
    private lateinit var screensNavigator: ScreensNavigator
    private lateinit var dialogsManager: DialogsManager
    private lateinit var viewMvc: IQuestionDetailsViewMvc

    companion object {

        @JvmStatic
        fun newInstance(questionId: String): QuestionDetailsFragment {
            return QuestionDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_QUESTION_ID, questionId)
                }
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
        screensNavigator = getCompositionRoot().getScreensNavigator()
        dialogsManager = getCompositionRoot().getDialogsManager()

        viewMvc = getCompositionRoot()
            .getViewMvcFactory()
            .getQuestionDetailsViewMvc(container)

        return viewMvc.getRootView()
    }

    override fun onStart() {
        super.onStart()

        fetchQuestionDetailsUseCase.registerListener(this)

        viewMvc.registerListener(this)
        viewMvc.showProgressIndicator(true)

        fetchQuestionDetails(questionId!!)
    }

    override fun onStop() {
        super.onStop()

        fetchQuestionDetailsUseCase.unregisterListener(this)
        viewMvc.unregisterListener(this)
    }

    override fun onNavigateUpClicked() {
        screensNavigator.navigateUp()
    }

    override fun onQuestionDetailsFetched(question: Question) {
        viewMvc.showProgressIndicator(false)
        viewMvc.bindQuestion(question)
    }

    override fun onQuestionDetailsFetchFailed() {
        viewMvc.showProgressIndicator(false)
        dialogsManager.showUseCaseError(null)
    }

    private fun fetchQuestionDetails(questionId: String) {
        fetchQuestionDetailsUseCase.executeAndNotify(questionId)
    }
}