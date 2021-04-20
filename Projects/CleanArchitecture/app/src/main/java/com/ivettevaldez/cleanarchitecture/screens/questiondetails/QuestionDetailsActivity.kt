package com.ivettevaldez.cleanarchitecture.screens.questiondetails

import android.os.Bundle
import android.widget.Toast
import com.ivettevaldez.cleanarchitecture.R
import com.ivettevaldez.cleanarchitecture.questions.FetchQuestionDetailsUseCase
import com.ivettevaldez.cleanarchitecture.questions.Question
import com.ivettevaldez.cleanarchitecture.screens.common.controllers.BaseActivity
import com.ivettevaldez.cleanarchitecture.screens.common.navigation.ScreenNavigator

class QuestionDetailsActivity : BaseActivity(),
    IQuestionDetailsViewMvc.Listener,
    FetchQuestionDetailsUseCase.Listener {

    private lateinit var fetchQuestionDetailsUseCase: FetchQuestionDetailsUseCase
    private lateinit var screenNavigator: ScreenNavigator
    private lateinit var viewMvc: IQuestionDetailsViewMvc

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fetchQuestionDetailsUseCase = getCompositionRoot()
            .getFetchQuestionDetailsUseCase()

        screenNavigator = getCompositionRoot()
            .getScreenNavigator()

        viewMvc = getCompositionRoot()
            .getViewMvcFactory()
            .getQuestionDetailsViewMvc(null)

        setContentView(viewMvc.getRootView())
    }

    override fun onStart() {
        super.onStart()

        fetchQuestionDetailsUseCase.registerListener(this)

        viewMvc.registerListener(this)
        viewMvc.showProgressIndicator(true)

        fetchQuestionDetails(getQuestionId())
    }

    override fun onStop() {
        super.onStop()
        fetchQuestionDetailsUseCase.unregisterListener(this)
        viewMvc.unregisterListener(this)
    }

    override fun onBackPressed() {
        if (viewMvc.isDrawerOpen()) {
            viewMvc.closeDrawer()
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigateUpClicked() {
        onBackPressed()
    }

    override fun onQuestionsListClicked() {
        screenNavigator.toQuestionsListAndClearTop()
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

    private fun getQuestionId(): String {
        intent.extras?.takeIf { it.containsKey(ScreenNavigator.EXTRA_QUESTION_ID) }?.apply {
            return getString(ScreenNavigator.EXTRA_QUESTION_ID) ?: ""
        }
        return ""
    }

    private fun fetchQuestionDetails(questionId: String) {
        fetchQuestionDetailsUseCase.executeAndNotify(questionId)
    }

    private fun showMessage(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }
}