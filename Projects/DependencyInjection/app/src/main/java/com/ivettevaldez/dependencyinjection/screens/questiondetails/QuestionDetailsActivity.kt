package com.ivettevaldez.dependencyinjection.screens.questiondetails

/* ktlint-disable no-wildcard-imports */

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.ivettevaldez.dependencyinjection.questions.FetchQuestionDetailsUseCase
import com.ivettevaldez.dependencyinjection.screens.common.controllers.BaseActivity
import com.ivettevaldez.dependencyinjection.screens.common.dialogs.DialogsNavigator
import com.ivettevaldez.dependencyinjection.screens.common.navigation.IScreensNavigator
import com.ivettevaldez.dependencyinjection.screens.common.viewsmvc.ViewMvcFactory
import kotlinx.coroutines.*
import javax.inject.Inject

private const val EXTRA_QUESTION_ID = "EXTRA_QUESTION_ID"

class QuestionDetailsActivity : BaseActivity(),
    IQuestionDetailsViewMvc.Listener {

    private val classTag: String = this::class.java.simpleName
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    @Inject
    lateinit var screensNavigator: IScreensNavigator

    @Inject
    lateinit var dialogsNavigator: DialogsNavigator

    @Inject
    lateinit var fetchQuestionDetailsUseCase: FetchQuestionDetailsUseCase

    @Inject
    lateinit var viewMvcFactory: ViewMvcFactory

    private lateinit var viewMvc: QuestionDetailsViewMvcImpl

    private lateinit var questionId: String

    companion object {

        fun start(context: Context, questionId: String) {
            val intent = Intent(context, QuestionDetailsActivity::class.java)
            intent.putExtra(EXTRA_QUESTION_ID, questionId)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)
        super.onCreate(savedInstanceState)

        questionId = intent.extras!!.getString(EXTRA_QUESTION_ID)!!

        viewMvc = viewMvcFactory.newQuestionDetailsViewMvc(null)
        setContentView(viewMvc.rootView)
    }

    override fun onStart() {
        super.onStart()

        viewMvc.registerListener(this)
        fetchQuestionDetails()
    }

    override fun onStop() {
        super.onStop()

        viewMvc.unregisterListener(this)
        coroutineScope.coroutineContext.cancelChildren()
    }

    override fun onBackClicked() {
        screensNavigator.navigateBack()
    }

    private fun fetchQuestionDetails() {
        coroutineScope.launch {
            viewMvc.showProgressIndicator()

            try {
                when (val result = fetchQuestionDetailsUseCase.execute(questionId)) {
                    is FetchQuestionDetailsUseCase.Result.Success -> {
                        viewMvc.bindQuestion(result.question)
                    }
                    is FetchQuestionDetailsUseCase.Result.Failure -> {
                        onFetchFailed()
                    }
                }
            } catch (ex: Exception) {
                Log.e(classTag, "Attempting to fetch question details", ex)
                onFetchFailed()
            } finally {
                viewMvc.hideProgressIndicator()
            }
        }
    }

    private fun onFetchFailed() {
        dialogsNavigator.showServerErrorDialog()
    }
}