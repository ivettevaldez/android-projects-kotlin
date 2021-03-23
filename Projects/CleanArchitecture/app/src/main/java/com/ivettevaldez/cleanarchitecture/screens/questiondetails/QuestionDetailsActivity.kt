package com.ivettevaldez.cleanarchitecture.screens.questiondetails

import android.os.Bundle
import android.widget.Toast
import com.ivettevaldez.cleanarchitecture.R
import com.ivettevaldez.cleanarchitecture.networking.StackOverflowApi
import com.ivettevaldez.cleanarchitecture.networking.questions.QuestionDetailsResponseSchema
import com.ivettevaldez.cleanarchitecture.networking.questions.QuestionSchema
import com.ivettevaldez.cleanarchitecture.questions.Question
import com.ivettevaldez.cleanarchitecture.screens.common.controllers.BaseActivity
import com.ivettevaldez.cleanarchitecture.screens.common.navigation.ScreenNavigator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuestionDetailsActivity : BaseActivity() {

    private lateinit var stackOverflowApi: StackOverflowApi
    private lateinit var viewMvc: IQuestionDetailsViewMvc

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        stackOverflowApi = getCompositionRoot().getStackOverflowApi()

        viewMvc = getCompositionRoot()
            .getViewMvcFactory()
            .getQuestionDetailsViewMvc(null)

        setContentView(viewMvc.getRootView())
    }

    override fun onStart() {
        super.onStart()

        val questionId = getQuestionId()
        if (questionId != null) {
            viewMvc.showProgressIndicator(true)
            fetchQuestionDetails(questionId)
        } else {
            showMessage(getString(R.string.error_null_question))
        }
    }

    private fun getQuestionId(): String? {
        intent.extras?.takeIf { it.containsKey(ScreenNavigator.EXTRA_QUESTION_ID) }?.apply {
            return getString(ScreenNavigator.EXTRA_QUESTION_ID)
        }
        return null
    }

    private fun fetchQuestionDetails(questionId: String) {
        stackOverflowApi.fetchQuestionDetails(questionId)!!
            .enqueue(
                object : Callback<QuestionDetailsResponseSchema?> {
                    override fun onResponse(
                        call: Call<QuestionDetailsResponseSchema?>,
                        response: Response<QuestionDetailsResponseSchema?>
                    ) {
                        viewMvc.showProgressIndicator(false)

                        if (response.isSuccessful) {
                            bindQuestion(response.body()?.getQuestion())
                        } else {
                            networkCallFailed()
                        }
                    }

                    override fun onFailure(
                        call: Call<QuestionDetailsResponseSchema?>,
                        throwable: Throwable
                    ) {
                        viewMvc.showProgressIndicator(false)
                        networkCallFailed()
                    }
                }
            )
    }

    private fun bindQuestion(questionSchema: QuestionSchema?) {
        if (questionSchema != null) {
            viewMvc.bindQuestion(
                Question(
                    questionSchema.id,
                    questionSchema.title,
                    questionSchema.body
                )
            )
        } else {
            showMessage(getString(R.string.error_oops))
        }
    }

    private fun networkCallFailed() {
        showMessage(getString(R.string.error_network_callback_failed))
    }

    private fun showMessage(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }
}