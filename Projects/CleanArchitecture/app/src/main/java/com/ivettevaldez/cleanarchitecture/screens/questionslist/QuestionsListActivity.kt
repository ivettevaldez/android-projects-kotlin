package com.ivettevaldez.cleanarchitecture.screens.questionslist

import android.os.Bundle
import android.widget.Toast
import com.ivettevaldez.cleanarchitecture.R
import com.ivettevaldez.cleanarchitecture.questions.FetchQuestionsUseCase
import com.ivettevaldez.cleanarchitecture.questions.Question
import com.ivettevaldez.cleanarchitecture.screens.common.controllers.BaseActivity

class QuestionsListActivity : BaseActivity(),
    IQuestionsListViewMvc.Listener,
    FetchQuestionsUseCase.Listener {

    private lateinit var fetchQuestionsUseCase: FetchQuestionsUseCase
    private lateinit var viewMvc: IQuestionsListViewMvc

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fetchQuestionsUseCase = getCompositionRoot()
            .getFetchQuestionsUseCase()

        viewMvc = getCompositionRoot()
            .getViewMvcFactory()
            .getQuestionsListViewMvc(null)

        setContentView(viewMvc.getRootView())
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

    override fun onBackPressed() {
        if (viewMvc.isDrawerOpen()) {
            viewMvc.closeDrawer()
        } else {
            super.onBackPressed()
        }
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

    override fun onQuestionsListClicked() {
        // This is the QuestionsList screen, so no action needed.
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
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }
}